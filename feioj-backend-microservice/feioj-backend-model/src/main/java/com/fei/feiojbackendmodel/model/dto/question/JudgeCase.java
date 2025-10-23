package com.fei.feiojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * 题目用例
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;
    /**
     * 输出用例
     */
    private String output;
    /**
     * 判题规则
     */
    private String judgeType;
}
