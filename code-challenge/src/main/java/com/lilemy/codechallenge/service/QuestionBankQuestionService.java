package com.lilemy.codechallenge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.codechallenge.model.dto.questionbankquestion.QuestionBankQuestionQueryRequest;
import com.lilemy.codechallenge.model.entity.QuestionBankQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.codechallenge.model.vo.QuestionBankQuestionVO;

/**
 * @author qq233
 * @description 针对表【question_bank_question(题库题目)】的数据库操作Service
 */
public interface QuestionBankQuestionService extends IService<QuestionBankQuestion> {

    /**
     * 校验数据
     *
     * @param questionBankQuestion 题库题目关系
     * @param add                  对创建的数据进行校验
     */
    void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion, boolean add);

    /**
     * 获取题库题目关系封装
     *
     * @param questionBankQuestion 题库题目关系
     * @return 题库题目关系封装
     */
    QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion);

    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest 题库题目关系查询请求体
     * @return 题库题目关系查询条件
     */
    QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest);

    /**
     * 分页获取题库题目关联封装
     *
     * @param questionBankQuestionPage 题库题目关联分页
     * @return 题库题目关联分页封装
     */
    Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage);
}
