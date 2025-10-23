package com.fei.feiojbackendjudgeservice.judge.strategy;


import com.fei.feiojbackendmodel.model.codeSandbox.JudgeInfo;
import com.fei.feiojbackendmodel.model.dto.question.JudgeCase;
import com.fei.feiojbackendmodel.model.entity.QuestionSubmit;
import com.fei.feiojbackendmodel.model.entity.Question;
import lombok.Data;

import java.util.List;
/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;
    private List<String> outputList;
    private List<String> inputList;
    private List<JudgeCase> judgeCaseList;
    private Question question;
    private QuestionSubmit questionSubmit;
    /**
     * 错误信息
     */
    private String errorMessage;
}
