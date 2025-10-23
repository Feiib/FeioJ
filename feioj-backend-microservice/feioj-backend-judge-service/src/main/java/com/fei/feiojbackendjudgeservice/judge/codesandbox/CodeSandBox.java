package com.fei.feiojbackendjudgeservice.judge.codesandbox;


import com.fei.feiojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.fei.feiojbackendmodel.model.codeSandbox.ExecuteCodeResponse;

public interface CodeSandBox {
    /**
     * 执行代码
     * @param request
     * @return
     */
    ExecuteCodeResponse execute(ExecuteCodeRequest request);
}
