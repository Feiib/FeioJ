package com.fei.feiojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fei.feiojbackendcommon.common.ErrorCode;
import com.fei.feiojbackendcommon.exception.BusinessException;
import com.fei.feiojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.fei.feiojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.fei.feiojbackendmodel.model.codeSandbox.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * Java语言专用代码沙箱
 */
public class JavaCodeSandBox implements CodeSandBox {
    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";
    
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("Java代码沙箱");
        String url = "http://feioj-codesandbox:8090/executeCode/java";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .header("Content-Type", "application/json")
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode Java sandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}