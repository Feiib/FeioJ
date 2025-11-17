package com.fei.feiojcodesandbox.sandbox.base;


import com.fei.feiojcodesandbox.model.ExecuteCodeRequest;
import com.fei.feiojcodesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {
    /**
     * 执行代码
     * @param request
     * @return
     */
    ExecuteCodeResponse execute(ExecuteCodeRequest request);
}
