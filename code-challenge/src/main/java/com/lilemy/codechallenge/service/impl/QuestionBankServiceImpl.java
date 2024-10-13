package com.lilemy.codechallenge.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.constant.CommonConstant;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.mapper.QuestionBankMapper;
import com.lilemy.codechallenge.model.dto.questionbank.QuestionBankAddRequest;
import com.lilemy.codechallenge.model.dto.questionbank.QuestionBankQueryRequest;
import com.lilemy.codechallenge.model.dto.questionbank.QuestionBankUpdateRequest;
import com.lilemy.codechallenge.model.entity.QuestionBank;
import com.lilemy.codechallenge.model.entity.QuestionBankQuestion;
import com.lilemy.codechallenge.model.entity.User;
import com.lilemy.codechallenge.model.vo.QuestionBankVO;
import com.lilemy.codechallenge.model.vo.UserVO;
import com.lilemy.codechallenge.service.QuestionBankQuestionService;
import com.lilemy.codechallenge.service.QuestionBankService;
import com.lilemy.codechallenge.service.UserService;
import com.lilemy.codechallenge.util.SqlUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qq233
 * @description 针对表【question_bank(题库)】的数据库操作Service实现
 */
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank>
        implements QuestionBankService {

    @Resource
    private UserService userService;

    @Resource
    private QuestionBankQuestionService questionBankQuestionService;

    @Override
    public void validQuestionBank(QuestionBank questionBank, boolean add) {
        ThrowUtils.throwIf(questionBank == null, ResultCode.PARAMS_ERROR);
        String title = questionBank.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(title), ResultCode.PARAMS_ERROR, "标题不能为空");
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ResultCode.PARAMS_ERROR, "标题过长");
        }
    }

    @Override
    public Long addQuestionBank(QuestionBankAddRequest questionBankAddRequest) {
        QuestionBank questionBank = new QuestionBank();
        BeanUtils.copyProperties(questionBankAddRequest, questionBank);
        // 数据校验
        this.validQuestionBank(questionBank, true);
        // 填充默认值
        User loginUser = userService.getLoginUser();
        questionBank.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = this.save(questionBank);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        return questionBank.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteQuestionBank(Long id) {
        // 删除关联题目
        LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .eq(QuestionBankQuestion::getQuestionBankId, id);
        List<QuestionBankQuestion> questionBankQuestions = questionBankQuestionService.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(questionBankQuestions)) {
            questionBankQuestionService.removeBatchByIds(questionBankQuestions);
        }
        // 操作数据库，删除题库
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public Boolean updateQuestionBank(QuestionBankUpdateRequest questionBankUpdateRequest) {
        Long id = questionBankUpdateRequest.getId();
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        QuestionBank questionBank = new QuestionBank();
        BeanUtils.copyProperties(questionBankUpdateRequest, questionBank);
        // 数据校验
        this.validQuestionBank(questionBank, false);
        // 判断是否存在
        QuestionBank oldQuestionBank = this.getById(id);
        ThrowUtils.throwIf(oldQuestionBank == null, ResultCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = this.updateById(questionBank);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest) {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        if (questionBankQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionBankQueryRequest.getId();
        Long notId = questionBankQueryRequest.getNotId();
        String searchText = questionBankQueryRequest.getSearchText();
        String title = questionBankQueryRequest.getTitle();
        String description = questionBankQueryRequest.getDescription();
        String picture = questionBankQueryRequest.getPicture();
        Long userId = questionBankQueryRequest.getUserId();
        String sortField = questionBankQueryRequest.getSortField();
        String sortOrder = questionBankQueryRequest.getSortOrder();
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("description", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(picture), "picture", picture);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    @Override
    public QuestionBankVO getQuestionBankVO(QuestionBank questionBank) {
        // 对象转封装类
        QuestionBankVO questionBankVO = QuestionBankVO.objToVo(questionBank);
        // 关联查询用户信息
        Long userId = questionBank.getUserId();
        UserVO userVO = userService.getUserVO(userId);
        questionBankVO.setUser(userVO);
        return questionBankVO;
    }

    @Override
    public Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage) {
        List<QuestionBank> questionBankList = questionBankPage.getRecords();
        Page<QuestionBankVO> questionBankVOPage = new Page<>(questionBankPage.getCurrent(), questionBankPage.getSize(), questionBankPage.getTotal());
        if (CollUtil.isEmpty(questionBankList)) {
            return questionBankVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankVO> questionBankVOList = questionBankList.stream().map(QuestionBankVO::objToVo).collect(Collectors.toList());
        // 关联查询用户信息
        Set<Long> userIdSet = questionBankList.stream().map(QuestionBank::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionBankVOList.forEach(questionBankVO -> {
            Long userId = questionBankVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionBankVO.setUser(userService.getUserVO(user));
        });
        questionBankVOPage.setRecords(questionBankVOList);
        return questionBankVOPage;
    }
}