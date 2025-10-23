package com.fei.feiojbackendjudgeservice.judge.strategy.answer;

/**
 * 精确匹配
 */
public class ExactMatchJudgeStrategy implements AnswerJudgeStrategy {
    @Override
    public boolean judge(String expected, String actual) {
        return expected.trim().equals(actual.trim());
    }
}
