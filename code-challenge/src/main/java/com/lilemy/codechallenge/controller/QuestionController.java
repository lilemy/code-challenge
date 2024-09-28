package com.lilemy.codechallenge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.codechallenge.common.BaseResponse;
import com.lilemy.codechallenge.common.DeleteRequest;
import com.lilemy.codechallenge.common.ResultCode;
import com.lilemy.codechallenge.common.ResultUtils;
import com.lilemy.codechallenge.exception.ThrowUtils;
import com.lilemy.codechallenge.model.dto.question.QuestionAddRequest;
import com.lilemy.codechallenge.model.dto.question.QuestionEditRequest;
import com.lilemy.codechallenge.model.dto.question.QuestionQueryRequest;
import com.lilemy.codechallenge.model.dto.question.QuestionUpdateRequest;
import com.lilemy.codechallenge.model.entity.Question;
import com.lilemy.codechallenge.model.entity.User;
import com.lilemy.codechallenge.model.vo.QuestionVO;
import com.lilemy.codechallenge.service.QuestionService;
import com.lilemy.codechallenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "QuestionController")
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    // region 题目增删改查

    @Operation(summary = "创建题目")
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest) {
        ThrowUtils.throwIf(questionAddRequest == null, ResultCode.PARAMS_ERROR);
        Long addQuestionId = questionService.addQuestion(questionAddRequest);
        return ResultUtils.success(addQuestionId);
    }

    @Operation(summary = "删除题目")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ResultCode.PARAMS_ERROR);
        Long deleteRequestId = deleteRequest.getId();
        ThrowUtils.throwIf(deleteRequestId <= 0, ResultCode.PARAMS_ERROR);
        boolean result = questionService.deleteQuestion(deleteRequestId);
        return ResultUtils.success(result);
    }

    @Operation(summary = "编辑题目")
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest) {
        ThrowUtils.throwIf(questionEditRequest == null, ResultCode.PARAMS_ERROR);
        ThrowUtils.throwIf(questionEditRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        boolean result = questionService.editQuestion(questionEditRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新题目")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        ThrowUtils.throwIf(questionUpdateRequest == null, ResultCode.PARAMS_ERROR);
        ThrowUtils.throwIf(questionUpdateRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        boolean result = questionService.updateQuestion(questionUpdateRequest);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据 id 获取题目（封装类）")
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(Long id) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ResultCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVO(question));
    }

    @Operation(summary = "分页获取题目列表（仅管理员可用）")
    @PostMapping("/list")
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null, ResultCode.PARAMS_ERROR);
        ThrowUtils.throwIf(!userService.isAdmin(), ResultCode.NO_AUTH_ERROR);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    @Operation(summary = "分页获取题目列表（封装类）")
    @PostMapping("/list/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null, ResultCode.PARAMS_ERROR);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCode.PARAMS_ERROR);
        // 查询数据库
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage));
    }

    @Operation(summary = "分页获取当前登录用户创建的题目列表")
    @PostMapping("/list/my/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null, ResultCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser();
        questionQueryRequest.setUserId(loginUser.getId());
        return listQuestionVOByPage(questionQueryRequest);
    }

    // endregion

    // region 题目审核
    // todo 题目审核
    // endregion
}
