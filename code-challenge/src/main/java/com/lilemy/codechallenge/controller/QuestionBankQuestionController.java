package com.lilemy.codechallenge.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lilemy.codechallenge.common.BaseResponse;
import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.common.ResultUtils;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.model.dto.questionbankquestion.QuestionBankQuestionAddRequest;
import com.lilemy.codechallenge.model.dto.questionbankquestion.QuestionBankQuestionRemoveRequest;
import com.lilemy.codechallenge.model.entity.QuestionBankQuestion;
import com.lilemy.codechallenge.model.entity.User;
import com.lilemy.codechallenge.service.QuestionBankQuestionService;
import com.lilemy.codechallenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "QuestionBankQuestionController")
@RequestMapping("/questionBankQuestion")
public class QuestionBankQuestionController {

    @Resource
    private QuestionBankQuestionService questionBankQuestionService;

    @Resource
    private UserService userService;

    @Operation(summary = "创建题库题目关联")
    @PostMapping("/add")
    public BaseResponse<Long> addQuestionBankQuestion(@RequestBody QuestionBankQuestionAddRequest questionBankQuestionAddRequest) {
        ThrowUtils.throwIf(questionBankQuestionAddRequest == null, ResultCode.PARAMS_ERROR);
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        BeanUtils.copyProperties(questionBankQuestionAddRequest, questionBankQuestion);
        // 填充默认值
        User loginUser = userService.getLoginUser();
        questionBankQuestion.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionBankQuestionService.save(questionBankQuestion);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionBankQuestionId = questionBankQuestion.getId();
        return ResultUtils.success(newQuestionBankQuestionId);
    }

    @Operation(summary = "移除题库题目关联")
    @PostMapping("/remove")
    public BaseResponse<Boolean> removeQuestionBankQuestion(
            @RequestBody QuestionBankQuestionRemoveRequest questionBankQuestionRemoveRequest
    ) {
        // 参数校验
        ThrowUtils.throwIf(questionBankQuestionRemoveRequest == null, ResultCode.PARAMS_ERROR);
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        Long questionBankId = questionBankQuestionRemoveRequest.getQuestionBankId();
        Long questionId = questionBankQuestionRemoveRequest.getQuestionId();
        ThrowUtils.throwIf(questionBankId == null || questionId == null, ResultCode.PARAMS_ERROR);
        // 构造查询
        LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
                .eq(QuestionBankQuestion::getQuestionId, questionId);
        boolean result = questionBankQuestionService.remove(lambdaQueryWrapper);
        return ResultUtils.success(result);
    }
}
