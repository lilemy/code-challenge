package com.lilemy.codechallenge.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.codechallenge.annotation.AuthCheck;
import com.lilemy.codechallenge.common.BaseResponse;
import com.lilemy.codechallenge.common.DeleteRequest;
import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.common.ResultUtils;
import com.lilemy.codechallenge.constant.UserConstant;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.model.dto.question.QuestionQueryRequest;
import com.lilemy.codechallenge.model.dto.questionbank.QuestionBankAddRequest;
import com.lilemy.codechallenge.model.dto.questionbank.QuestionBankQueryRequest;
import com.lilemy.codechallenge.model.dto.questionbank.QuestionBankUpdateRequest;
import com.lilemy.codechallenge.model.entity.Question;
import com.lilemy.codechallenge.model.entity.QuestionBank;
import com.lilemy.codechallenge.model.vo.QuestionBankListVO;
import com.lilemy.codechallenge.model.vo.QuestionBankVO;
import com.lilemy.codechallenge.model.vo.QuestionVO;
import com.lilemy.codechallenge.service.QuestionBankService;
import com.lilemy.codechallenge.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "QuestionBankController")
@RequestMapping("/questionBank")
public class QuestionBankController {

    @Resource
    private QuestionBankService questionBankService;

    @Resource
    private QuestionService questionService;

    // region 增删改查

    @Operation(summary = "添加题库")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestionBank(@RequestBody QuestionBankAddRequest questionBankAddRequest) {
        ThrowUtils.throwIf(questionBankAddRequest == null, ResultCode.PARAMS_ERROR);
        Long addQuestionBankId = questionBankService.addQuestionBank(questionBankAddRequest);
        return ResultUtils.success(addQuestionBankId);
    }

    @Operation(summary = "删除题库")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestionBank(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ResultCode.PARAMS_ERROR);
        Long questionBankId = deleteRequest.getId();
        ThrowUtils.throwIf(questionBankId <= 0, ResultCode.PARAMS_ERROR);
        Boolean result = questionBankService.deleteQuestionBank(questionBankId);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新题库")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionBank(@RequestBody QuestionBankUpdateRequest questionBankUpdateRequest) {
        ThrowUtils.throwIf(questionBankUpdateRequest == null, ResultCode.PARAMS_ERROR);
        Boolean result = questionBankService.updateQuestionBank(questionBankUpdateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据id获取题库脱敏信息")
    @GetMapping("/get/vo")
    public BaseResponse<QuestionBankVO> getQuestionBankVOById(Long id, Boolean isNeedQueryQuestionList) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        // 查询数据库
        QuestionBank questionBank = questionBankService.getById(id);
        ThrowUtils.throwIf(questionBank == null, ResultCode.NOT_FOUND_ERROR);
        QuestionBankVO questionBankVO = questionBankService.getQuestionBankVO(questionBank);
        // 是否要关联查询题库下的题目列表
        if (isNeedQueryQuestionList) {
            QuestionQueryRequest questionQueryRequest = new QuestionQueryRequest();
            questionQueryRequest.setQuestionBankId(id);
            Page<Question> questionPage = questionService.listQuestionByPage(questionQueryRequest, false);
            Page<QuestionVO> questionVOPage = questionService.getQuestionVOPage(questionPage);
            questionBankVO.setQuestionPage(questionVOPage);
        }
        // 获取封装类
        return ResultUtils.success(questionBankVO);
    }

    @Operation(summary = "获取题库列表信息")
    @GetMapping("/get/list")
    public BaseResponse<List<QuestionBankListVO>> getQuestionBankList() {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title", "description");
        List<QuestionBank> list = questionBankService.list(queryWrapper);
        List<QuestionBankListVO> questionBankListVOList = list.stream().map(questionBank -> {
            QuestionBankListVO questionBankListVO = new QuestionBankListVO();
            questionBankListVO.setId(questionBank.getId());
            questionBankListVO.setTitle(questionBank.getTitle());
            questionBankListVO.setDescription(questionBank.getDescription());
            return questionBankListVO;
        }).toList();
        return ResultUtils.success(questionBankListVOList);
    }

    @Operation(summary = "分页获取题库列表（仅管理员可用）")
    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionBank>> listQuestionBankByPage(@RequestBody QuestionBankQueryRequest questionBankQueryRequest) {
        long current = questionBankQueryRequest.getCurrent();
        long size = questionBankQueryRequest.getPageSize();
        // 查询数据库
        Page<QuestionBank> questionBankPage = questionBankService.page(new Page<>(current, size),
                questionBankService.getQueryWrapper(questionBankQueryRequest));
        return ResultUtils.success(questionBankPage);
    }

    @Operation(summary = "分页获取题库列表（封装类）")
    @PostMapping("/list/vo")
    public BaseResponse<Page<QuestionBankVO>> listQuestionBankVOByPage(@RequestBody QuestionBankQueryRequest questionBankQueryRequest) {
        long current = questionBankQueryRequest.getCurrent();
        long size = questionBankQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 200, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionBank> questionBankPage = questionBankService.page(new Page<>(current, size),
                questionBankService.getQueryWrapper(questionBankQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionBankService.getQuestionBankVOPage(questionBankPage));
    }

    // endregion

}
