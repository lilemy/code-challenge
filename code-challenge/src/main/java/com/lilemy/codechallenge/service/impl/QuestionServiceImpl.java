package com.lilemy.codechallenge.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.constant.CommonConstant;
import com.lilemy.codechallenge.exception.BusinessException;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.mapper.QuestionMapper;
import com.lilemy.codechallenge.model.dto.question.*;
import com.lilemy.codechallenge.model.entity.Question;
import com.lilemy.codechallenge.model.entity.QuestionBank;
import com.lilemy.codechallenge.model.entity.QuestionBankQuestion;
import com.lilemy.codechallenge.model.entity.User;
import com.lilemy.codechallenge.model.enums.ReviewStatusEnum;
import com.lilemy.codechallenge.model.vo.QuestionBankListVO;
import com.lilemy.codechallenge.model.vo.QuestionPersonalVO;
import com.lilemy.codechallenge.model.vo.QuestionVO;
import com.lilemy.codechallenge.model.vo.UserVO;
import com.lilemy.codechallenge.service.QuestionBankQuestionService;
import com.lilemy.codechallenge.service.QuestionBankService;
import com.lilemy.codechallenge.service.QuestionService;
import com.lilemy.codechallenge.service.UserService;
import com.lilemy.codechallenge.util.SqlUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qq233
 * @description 针对表【question(题目)】的数据库操作Service实现
 */
@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private QuestionBankService questionBankService;

    @Resource
    private QuestionBankQuestionService questionBankQuestionService;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void validQuestion(Question question, boolean add) {
        ThrowUtils.throwIf(question == null, ResultCode.PARAMS_ERROR);
        // 从对象中取值
        String title = question.getTitle();
        String content = question.getContent();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ResultCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ResultCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content)) {
            ThrowUtils.throwIf(content.length() > 10240, ResultCode.PARAMS_ERROR, "内容过长");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createQuestion(QuestionCreateRequest questionCreateRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionCreateRequest, question);
        List<String> tags = questionCreateRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        // 填充默认值
        User loginUser = userService.getLoginUser();
        question.setUserId(loginUser.getId());
        // 数据校验
        this.validQuestion(question, true);
        // 写入数据库
        boolean result = this.save(question);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        // 添加题目题库关联
        List<Long> questionBankIds = questionCreateRequest.getQuestionBankIds();
        if (CollUtil.isNotEmpty(questionBankIds)) {
            for (Long questionBankId : questionBankIds) {
                QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
                questionBankQuestion.setQuestionId(question.getId());
                questionBankQuestion.setQuestionBankId(questionBankId);
                questionBankQuestion.setUserId(loginUser.getId());
                boolean save = questionBankQuestionService.save(questionBankQuestion);
                ThrowUtils.throwIf(!save, ResultCode.OPERATION_ERROR);
            }
        }
        return question.getId();
    }

    @Override
    public Long addQuestion(QuestionAddRequest questionAddRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        // 数据校验
        this.validQuestion(question, true);
        // 填充默认值
        User loginUser = userService.getLoginUser();
        question.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = this.save(question);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return question.getId();
    }

    @Override
    public boolean deleteQuestion(Long deleteRequestId) {
        // 判断是否存在
        Question oldQuestion = this.getById(deleteRequestId);
        ThrowUtils.throwIf(oldQuestion == null, ResultCode.NOT_FOUND_ERROR);
        User loginUser = userService.getLoginUser();
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin()) {
            throw new BusinessException(ResultCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.removeById(deleteRequestId);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editQuestion(QuestionEditRequest questionEditRequest) {
        // 判断是否存在
        Long id = questionEditRequest.getId();
        Question oldQuestion = this.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ResultCode.NOT_FOUND_ERROR);
        User loginUser = userService.getLoginUser();
        // 仅本人可编辑
        ThrowUtils.throwIf(!oldQuestion.getUserId().equals(loginUser.getId()), ResultCode.NO_AUTH_ERROR);
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        LambdaUpdateChainWrapper<Question> lambdaUpdate = this.lambdaUpdate();
        lambdaUpdate.eq(Question::getId, id);
        // 设置题目标签
        List<String> tags = questionEditRequest.getTags();
        if (tags != null) {
            lambdaUpdate.set(Question::getTags, JSONUtil.toJsonStr(tags));
        }
        String title = questionEditRequest.getTitle();
        String content = questionEditRequest.getContent();
        String answer = questionEditRequest.getAnswer();
        lambdaUpdate.set(StringUtils.isNotBlank(title), Question::getTitle, title);
        lambdaUpdate.set(StringUtils.isNotBlank(content), Question::getContent, content);
        lambdaUpdate.set(StringUtils.isNotBlank(answer), Question::getAnswer, answer);
        // 设置题库题目关联
        List<Long> questionBankList = questionEditRequest.getQuestionBankList();
        // 获取题目题库关联的题库 id
        LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .select(QuestionBankQuestion::getQuestionBankId)
                .eq(QuestionBankQuestion::getQuestionId, id);
        List<Long> bankIds = questionBankQuestionService.listObjs(lambdaQueryWrapper, obj -> (Long) obj);
        // 获取需要添加的题目题库关联
        List<Long> addIdList = new ArrayList<>(questionBankList);
        addIdList.removeAll(bankIds);
        if (CollUtil.isNotEmpty(addIdList)) {
            List<QuestionBankQuestion> questionBankQuestionList = addIdList.stream()
                    .map(bankId -> {
                        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
                        questionBankQuestion.setQuestionId(id);
                        questionBankQuestion.setQuestionBankId(bankId);
                        // 设置默认值
                        questionBankQuestion.setUserId(loginUser.getId());
                        return questionBankQuestion;
                    })
                    .toList();
            questionBankQuestionService.saveBatch(questionBankQuestionList);
        }
        // 获取需要添加的题目题库关联
        List<Long> deleteIdList = new ArrayList<>(bankIds);
        deleteIdList.removeAll(questionBankList);
        if (CollUtil.isNotEmpty(deleteIdList)) {
            QueryWrapper<QuestionBankQuestion> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("question_id", id);
            queryWrapper.in("question_bank_id", deleteIdList);
            questionBankQuestionService.remove(queryWrapper);
        }
        // 设置编辑时间
        lambdaUpdate.set(Question::getEditTime, new Date());
        // 需重新审核
        if (!oldQuestion.getReviewStatus().equals(ReviewStatusEnum.REVIEWING.getValue())) {
            lambdaUpdate
                    .set(Question::getReviewMessage, null)
                    .set(Question::getReviewStatus, ReviewStatusEnum.REVIEWING.getValue())
                    .set(Question::getReviewerId, null)
                    .set(Question::getReviewTime, null);
        }
        // 数据校验
        this.validQuestion(question, false);
        // 操作数据库，更新题目数据
        boolean update = lambdaUpdate.update();
        ThrowUtils.throwIf(!update, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        // 数据校验
        this.validQuestion(question, false);
        // 判断是否存在
        Long id = questionUpdateRequest.getId();
        Question oldQuestion = this.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ResultCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = this.updateById(question);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionQueryRequest.getId();
        Long notId = questionQueryRequest.getNotId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        String searchText = questionQueryRequest.getSearchText();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        List<String> tagList = questionQueryRequest.getTags();
        Long userId = questionQueryRequest.getUserId();
        String answer = questionQueryRequest.getAnswer();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    @Override
    public QueryWrapper<Question> getReviewQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = this.getQueryWrapper(questionQueryRequest);
        return queryWrapper.eq("review_status", ReviewStatusEnum.PASS.getValue());
    }

    @Override
    public QuestionVO getQuestionVO(Question question) {
        // 对象转封装类
        QuestionVO questionVO = QuestionVO.objToVo(question);
        // 关联查询用户信息
        Long userId = question.getUserId();
        UserVO userVO = userService.getUserVO(userId);
        questionVO.setUser(userVO);
        return questionVO;
    }

    @Override
    public QuestionPersonalVO getQuestionPersonalById(Long id) {
        // 查询数据库
        Question question = this.getById(id);
        ThrowUtils.throwIf(question == null, ResultCode.NOT_FOUND_ERROR);
        // 判断是否为创建用户
        Long loginUserId = userService.getLoginUser().getId();
        ThrowUtils.throwIf(!question.getUserId().equals(loginUserId), ResultCode.NO_AUTH_ERROR);
        QuestionPersonalVO questionPersonalVO = QuestionPersonalVO.objToVo(question);
        LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .select(QuestionBankQuestion::getQuestionBankId)
                .eq(QuestionBankQuestion::getQuestionId, id);
        List<Long> bankIdList = questionBankQuestionService.listObjs(lambdaQueryWrapper, obj -> (Long) obj);
        if (CollUtil.isNotEmpty(bankIdList)) {
            QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", bankIdList);
            queryWrapper.select("id", "title", "description");
            List<QuestionBankListVO> bankListVOList = questionBankService.list(queryWrapper).stream()
                    .map(QuestionBankListVO::objToVO)
                    .toList();
            questionPersonalVO.setQuestionBankList(bankListVOList);
        }
        return questionPersonalVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionVO> questionVOList = questionList.stream().map(QuestionVO::objToVo).collect(Collectors.toList());
        // 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionVOList.forEach(questionVO -> {
            Long userId = questionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUser(userService.getUserVO(user));
        });
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public Page<QuestionPersonalVO> getQuestionPersonalPage(Page<Question> questionPage) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionPersonalVO> questionPersonalVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionPersonalVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionPersonalVO> questionPersonalList = questionList.stream().map(QuestionPersonalVO::objToVo).collect(Collectors.toList());
        // 关联查询题库信息
        Map<Long, List<QuestionBankListVO>> questionBankListVO = questionList.stream().collect(Collectors.toMap(
                Question::getId,
                question -> {
                    Long questionId = question.getId();
                    LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                            .select(QuestionBankQuestion::getQuestionBankId)
                            .eq(QuestionBankQuestion::getQuestionId, questionId);
                    // 获取所属题库列表
                    List<Long> questionBankIdList = questionBankQuestionService.listObjs(lambdaQueryWrapper, obj -> (Long) obj);
                    if (CollUtil.isNotEmpty(questionBankIdList)) {
                        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
                        queryWrapper.in("id", questionBankIdList);
                        queryWrapper.select("id", "title", "description");
                        List<QuestionBank> list = questionBankService.list(queryWrapper);
                        return list.stream()
                                .map(QuestionBankListVO::objToVO)
                                .collect(Collectors.toList());
                    }
                    return Collections.emptyList();
                }));
        // 填充信息
        questionPersonalList.forEach(questionPersonalVO -> {
            Long questionId = questionPersonalVO.getId();
            List<QuestionBankListVO> questionBankListVOList = new ArrayList<>();
            if (questionBankListVO.containsKey(questionId)) {
                questionBankListVOList = questionBankListVO.get(questionId);
            }
            questionPersonalVO.setQuestionBankList(questionBankListVOList);
        });
        questionPersonalVOPage.setRecords(questionPersonalList);
        return questionPersonalVOPage;
    }

    @Override
    public Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest, Boolean isIncludeNoPass) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 题目表的查询条件
        QueryWrapper<Question> queryWrapper = this.getQueryWrapper(questionQueryRequest);
        // 根据题库查询题目列表接口
        Long questionBankId = questionQueryRequest.getQuestionBankId();
        if (questionBankId != null) {
            // 查询题库内的题目 id
            LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                    .select(QuestionBankQuestion::getQuestionId)
                    .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
            List<QuestionBankQuestion> questionList = questionBankQuestionService.list(lambdaQueryWrapper);
            if (CollUtil.isNotEmpty(questionList)) {
                // 取出题目 id 集合
                Set<Long> questionIdSet = questionList.stream()
                        .map(QuestionBankQuestion::getQuestionId)
                        .collect(Collectors.toSet());
                // 复用原有题目表的查询条件
                queryWrapper.in("id", questionIdSet);
                if (!isIncludeNoPass) {
                    queryWrapper.eq("review_status", ReviewStatusEnum.PASS.getValue());
                }
                // 查询数据库
                return this.page(new Page<>(current, size), queryWrapper);
            }
        }
        return new Page<>(current, size);
    }

    @Override
    public Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest) {
        // 获取参数
        Long id = questionQueryRequest.getId();
        Long notId = questionQueryRequest.getNotId();
        String searchText = questionQueryRequest.getSearchText();
        List<String> tags = questionQueryRequest.getTags();
        Long questionBankId = questionQueryRequest.getQuestionBankId();
        Long userId = questionQueryRequest.getUserId();
        // ES 的起始页为 0
        int current = questionQueryRequest.getCurrent() - 1;
        int pageSize = questionQueryRequest.getPageSize();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        // 构造查询条件
        // 排序
        // 分页
        /**
         * PageRequest pageRequest = PageRequest.of(current, pageSize);
        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery()
                .withPageable(pageRequest)
                .build();
        SearchHits<QuestionEsDTO> searchHits = elasticsearchTemplate.search(searchQuery, QuestionEsDTO.class);
        Page<Question> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<Question> resourceList = new ArrayList<>();
        if (searchHits.hasSearchHits()) {
            List<SearchHit<QuestionEsDTO>> searchHitList = searchHits.getSearchHits();
            for (SearchHit<QuestionEsDTO> questionEsDTOSearchHit : searchHitList) {
                resourceList.add(QuestionEsDTO.dtoToObj(questionEsDTOSearchHit.getContent()));
            }
        }
        page.setRecords(resourceList);*/
        return null;
    }

    @Override
    public boolean reviewQuestion(QuestionReviewRequest questionReviewRequest) {
        // 查询审核题目信息
        Long questionId = questionReviewRequest.getId();
        ThrowUtils.throwIf(questionId <= 0, ResultCode.PARAMS_ERROR);
        Question question = this.getById(questionId);
        ThrowUtils.throwIf(question == null, ResultCode.NOT_FOUND_ERROR, "题目不存在");
        // 获取审核信息
        Integer reviewStatus = questionReviewRequest.getReviewStatus();
        String reviewMessage = questionReviewRequest.getReviewMessage();
        // 如果审核信息为空，则添加默认信息
        if (StringUtils.isBlank(reviewMessage)) {
            // 如果审核通过
            if (reviewStatus == ReviewStatusEnum.PASS.getValue()) {
                reviewMessage = ReviewStatusEnum.PASS.getText();
            }
            // 如果审核不通过
            if (reviewStatus == ReviewStatusEnum.REJECT.getValue()) {
                reviewMessage = ReviewStatusEnum.REJECT.getText();
            }
        }
        // 获取审核人信息
        User loginUser = userService.getLoginUser();
        Long userId = loginUser.getId();
        // 获取审核时间
        question.setReviewTime(new Date());
        question.setReviewStatus(reviewStatus);
        question.setReviewMessage(reviewMessage);
        question.setReviewerId(userId);
        boolean result = this.updateById(question);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteQuestions(List<Long> questionIdList) {
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIdList), ResultCode.PARAMS_ERROR, "删除的题目列表为空");
        for (Long questionId : questionIdList) {
            boolean result = this.removeById(questionId);
            ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR, "删除题目失败");
            // 移除题目题库关系
            LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                    .eq(QuestionBankQuestion::getQuestionId, questionId);
            result = questionBankQuestionService.remove(lambdaQueryWrapper);
            ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR, "删除题目题库关联失败");
        }
        return true;
    }

}
