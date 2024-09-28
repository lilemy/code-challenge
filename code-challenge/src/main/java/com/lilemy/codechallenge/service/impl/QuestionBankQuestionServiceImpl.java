package com.lilemy.codechallenge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.codechallenge.model.entity.QuestionBankQuestion;
import com.lilemy.codechallenge.service.QuestionBankQuestionService;
import com.lilemy.codechallenge.mapper.QuestionBankQuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author qq233
* @description 针对表【question_bank_question(题库题目)】的数据库操作Service实现
*/
@Service
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion>
    implements QuestionBankQuestionService{

}




