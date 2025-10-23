package com.fei.feiojbackendjudgeservice.judge.strategy.language;

import cn.hutool.json.JSONUtil;
import com.fei.feiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.fei.feiojbackendjudgeservice.judge.strategy.answer.AnswerJudgeStrategy;
import com.fei.feiojbackendjudgeservice.judge.strategy.factory.AnswerJudgeStrategyFactory;
import com.fei.feiojbackendmodel.model.codeSandbox.JudgeInfo;
import com.fei.feiojbackendmodel.model.dto.question.JudgeCase;
import com.fei.feiojbackendmodel.model.dto.question.JudgeConfig;
import com.fei.feiojbackendmodel.model.entity.Question;
import com.fei.feiojbackendmodel.model.enums.JudgeInfoMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
@Component
@Slf4j
public class JavaJudgeStrategy implements JudgeStrategy {
    @Resource
    private AnswerJudgeStrategyFactory answerJudgeStrategyFactory;
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        List<String> outputList = judgeContext.getOutputList();

        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = new JudgeInfo();

        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setLanguage("java");
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setMessage(judgeContext.getErrorMessage());
        // 根据执行结果,得到判题信息

        // 校验输出数量与用例数量一致
        if (outputList == null || judgeCaseList == null || outputList.size() != judgeCaseList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setStatus(judgeInfoMessageEnum.getValue());
            judgeInfoResponse.setMessage(judgeContext.getErrorMessage());
            return judgeInfoResponse;
        }
        // 判断每一项的预期输出与实际输出结果是否一致
        for (int i = 0; i < outputList.size(); i++) {
            // 当前用例
            JudgeCase judgeCase = judgeCaseList.get(i);
            // 根据题目配置选择判题策略
            AnswerJudgeStrategy answerJudgeStrategy = answerJudgeStrategyFactory.getAnswerJudgeStrategy(judgeCase.getJudgeType());
            String expected = judgeCase.getOutput();
            String actual = outputList.get(i);
            log.info("expected="+expected);
            log.info("actual="+actual);
            if (!answerJudgeStrategy.judge(expected, actual)) {
                log.info("answerJudgeStrategy.judge = " + answerJudgeStrategy.judge(expected, actual));
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setStatus(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        //判断题目限制
        //JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();

        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        if (memory != null) {
            if (memory > memoryLimit) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setStatus(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        long JAVA_PROGRAM_TIME_COST = 0L;
        if (time - JAVA_PROGRAM_TIME_COST > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setStatus(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setStatus(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }

    @Override
    public String getLanguage() {
        return "java";
    }
}
