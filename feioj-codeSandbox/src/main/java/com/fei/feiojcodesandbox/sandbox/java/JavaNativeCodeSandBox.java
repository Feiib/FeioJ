package com.fei.feiojcodesandbox.sandbox.java;

import cn.hutool.core.io.resource.ResourceUtil;
import com.fei.feiojcodesandbox.model.ExecuteCodeRequest;
import com.fei.feiojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class JavaNativeCodeSandBox extends JavaCodeSandBoxTemplate {
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest request) {
        return super.execute(request);
    }
    public static void main(String[] args) {
        JavaNativeCodeSandBox javaNativeCodeSandBox = new JavaNativeCodeSandBox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 3", "2 3"));
        String code = ResourceUtil.readStr("testCode/SimpleCompute/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.execute(executeCodeRequest);
        System.out.println(executeCodeResponse);

    }
}
