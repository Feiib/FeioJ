package com.fei.feiojbackendjudgeservice.judge.strategy.answer;

/**
 * 浮点数比较（允许误差 ±1e-6）
 */
public class FloatJudgeStrategy implements AnswerJudgeStrategy {
    private static final double EPS = 1e-6;

    @Override
    public boolean judge(String expected, String actual) {
        try {
            double e = Double.parseDouble(expected.trim());
            double a = Double.parseDouble(actual.trim());
            return Math.abs(e - a) < EPS;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
