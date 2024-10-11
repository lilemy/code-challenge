package com.lilemy.codechallenge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.codechallenge.model.dto.question.*;
import com.lilemy.codechallenge.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.codechallenge.model.vo.QuestionVO;

import java.util.List;

/**
 * @author qq233
 * @description 针对表【question(题目)】的数据库操作Service
 */
public interface QuestionService extends IService<Question> {

    /**
     * 校验数据
     *
     * @param question 题目信息
     * @param add      对创建的数据进行校验
     */
    void validQuestion(Question question, boolean add);

    /**
     * 创建题目
     *
     * @param questionAddRequest 题目创建请求
     * @return 创建题目 id
     */
    Long addQuestion(QuestionAddRequest questionAddRequest);

    /**
     * 删除题目
     *
     * @param deleteRequestId 题目 id
     * @return 是否删除成功
     */
    boolean deleteQuestion(Long deleteRequestId);

    /**
     * 编辑题目
     *
     * @param questionEditRequest 题目编辑请求体
     * @return 是否编辑成功
     */
    boolean editQuestion(QuestionEditRequest questionEditRequest);

    /**
     * 更新题目
     *
     * @param questionUpdateRequest 题目更新请求体
     * @return 是否更新成功
     */
    boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest 题目查询请求体
     * @return 题目查询条件
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question 题目信息
     * @return 题目信息封装
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 分页获取题目封装
     *
     * @param questionPage 题目信息分页
     * @return 题目封装信息分页
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage);

    /**
     * 分页获取题目列表
     *
     * @param questionQueryRequest 题目查询请求体
     * @return 题目分页列表
     */
    Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * Es 搜索分页获取题目列表
     *
     * @param questionQueryRequest 题目查询请求体
     * @return 题目分页列表
     */
    Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest);

    /**
     * 题目审核
     *
     * @param questionReviewRequest 题目审核请求体
     * @return 是否操作成功
     */
    boolean reviewQuestion(QuestionReviewRequest questionReviewRequest);

    /**
     * 批量删除题目
     *
     * @param questionIdList 删除题目 id 列表
     * @return 是否操作成功
     */
    boolean batchDeleteQuestions(List<Long> questionIdList);
}
