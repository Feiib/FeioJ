package com.fei.feiojbackendjudgeservice.judge.strategy.factory;

import com.fei.feiojbackendjudgeservice.judge.strategy.answer.*;
import org.springframework.stereotype.Component;

@Component
public class AnswerJudgeStrategyFactory {
    public static AnswerJudgeStrategy getAnswerJudgeStrategy(String type) {
        switch (type) {
            case "EXACT":
                return new ExactMatchJudgeStrategy();
            case "MULTI":
                return new MultiAnswerJudgeStrategy();
            case "UNORDERED":
                return new UnorderedJudgeStrategy();
            case "FLOAT":
                return new FloatJudgeStrategy();
            default:
                return new ExactMatchJudgeStrategy();
        }
    }
}