package com.fei.feiojbackendjudgeservice.judge.strategy.answer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 多解匹配
 */
public class MultiAnswerJudgeStrategy implements AnswerJudgeStrategy {
    @Override
    public boolean judge(String expected, String actual) {
        // expected 可能是 "2|3|5"
        Set<String> validAnswers = new HashSet<>(Arrays.asList(expected.split("\\|")));
        return validAnswers.contains(actual.trim());
    }
}
