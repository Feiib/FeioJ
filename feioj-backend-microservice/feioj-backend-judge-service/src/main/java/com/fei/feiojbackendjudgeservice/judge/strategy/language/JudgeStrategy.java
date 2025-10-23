package com.fei.feiojbackendjudgeservice.judge.strategy.language;


import com.fei.feiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.fei.feiojbackendmodel.model.codeSandbox.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
    String getLanguage();
}
