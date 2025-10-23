package com.fei.feiojbackendjudgeservice.judge.strategy.language;

import cn.hutool.json.JSONUtil;
import com.fei.feiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.fei.feiojbackendmodel.model.codeSandbox.JudgeInfo;
import com.fei.feiojbackendmodel.model.dto.question.JudgeCase;
import com.fei.feiojbackendmodel.model.dto.question.JudgeConfig;
import com.fei.feiojbackendmodel.model.entity.Question;
import com.fei.feiojbackendmodel.model.enums.JudgeInfoMessageEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认判题策略
 */
@Component
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        List<String> outputList = judgeContext.getOutputList();
        List<String> inputList = judgeContext.getInputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);

        // 根据执行结果,得到判题信息

        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //判断每一项的预期输出与实际输出结果是否一致
        for (int i = 0; i < outputList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getInput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = judgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        //判断题目限制
        //JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();

        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        if (memory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (time > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        return judgeInfoResponse;
    }

    @Override
    public String getLanguage() {
        return "";
    }
}
