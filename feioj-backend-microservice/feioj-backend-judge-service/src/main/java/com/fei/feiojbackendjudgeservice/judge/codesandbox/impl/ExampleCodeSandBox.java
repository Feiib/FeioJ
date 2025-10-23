package com.fei.feiojbackendjudgeservice.judge.codesandbox.impl;

import com.fei.feiojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.fei.feiojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.fei.feiojbackendmodel.model.codeSandbox.ExecuteCodeResponse;
import com.fei.feiojbackendmodel.model.codeSandbox.JudgeInfo;
import com.fei.feiojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.fei.feiojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱,为了跑通业务流程
 */
public class ExampleCodeSandBox implements CodeSandBox {

    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("ExampleCodeSandBox");
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
