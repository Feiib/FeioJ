package com.fei.feiojbackendjudgeservice.judge;


import com.fei.feiojbackendjudgeservice.judge.strategy.factory.JudgeStrategyFactory;
import com.fei.feiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.fei.feiojbackendjudgeservice.judge.strategy.language.JudgeStrategy;
import com.fei.feiojbackendmodel.model.codeSandbox.JudgeInfo;
import com.fei.feiojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JudgeManager {
    @Resource
    private JudgeStrategyFactory judgeStrategyFactory;

    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = judgeStrategyFactory.getStrategy(language);
        return judgeStrategy.doJudge(judgeContext);
    }
}
