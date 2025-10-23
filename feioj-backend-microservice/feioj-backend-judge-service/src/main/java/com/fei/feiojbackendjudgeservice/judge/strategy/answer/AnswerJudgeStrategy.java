package com.fei.feiojbackendjudgeservice.judge.strategy.answer;

public interface AnswerJudgeStrategy {
    boolean judge(String expected, String actual);
}
