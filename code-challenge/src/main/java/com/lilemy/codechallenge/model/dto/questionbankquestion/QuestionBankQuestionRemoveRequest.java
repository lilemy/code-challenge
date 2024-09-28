package com.lilemy.codechallenge.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 移除题目题库关系请求
 */
@Data
public class QuestionBankQuestionRemoveRequest  implements Serializable {
    @Serial
    private static final long serialVersionUID = 210768897728966299L;

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private Long questionId;
}
