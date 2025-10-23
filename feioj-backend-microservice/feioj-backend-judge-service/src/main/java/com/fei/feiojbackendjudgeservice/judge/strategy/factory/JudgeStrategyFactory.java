package com.fei.feiojbackendjudgeservice.judge.strategy.factory;

import com.fei.feiojbackendjudgeservice.judge.strategy.language.DefaultJudgeStrategy;
import com.fei.feiojbackendjudgeservice.judge.strategy.language.JudgeStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JudgeStrategyFactory {

    private final Map<String, JudgeStrategy> strategyMap = new HashMap<>();

    public JudgeStrategyFactory(List<JudgeStrategy> strategies) {
        for (JudgeStrategy strategy : strategies) {
            strategyMap.put(strategy.getLanguage(), strategy);
        }
    }

    public JudgeStrategy getStrategy(String language) {
        return strategyMap.getOrDefault(language, new DefaultJudgeStrategy());
    }
}
