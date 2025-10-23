package com.fei.feiojbackendjudgeservice.judge.codesandbox;

import com.fei.feiojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.fei.feiojbackendmodel.model.codeSandbox.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CodeSandBoxProxy implements CodeSandBox {
    private CodeSandBox codeSandBox;
    

    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest request) {

        
        log.info("Execute CodeSandBox request: {}", request.toString());
        ExecuteCodeResponse response = codeSandBox.execute(request);
        log.info("Execute CodeSandBox response: {}", response.toString());
        return response;
    }
}
