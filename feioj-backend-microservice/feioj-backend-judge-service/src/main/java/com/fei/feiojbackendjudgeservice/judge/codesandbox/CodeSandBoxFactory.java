package com.fei.feiojbackendjudgeservice.judge.codesandbox;

import com.fei.feiojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandBox;
import com.fei.feiojbackendjudgeservice.judge.codesandbox.impl.JavaCodeSandBox;
import com.fei.feiojbackendjudgeservice.judge.codesandbox.impl.PythonCodeSandBox;

/**
 * 代码沙箱工厂,根据type参数获取指定的代码沙箱参数
 */
public class CodeSandBoxFactory {
    
    /**
     * 根据类型获取代码沙箱实例
     * @param language 沙箱类型
     * @return 代码沙箱实例
     */
    public static CodeSandBox getInstance(String language) {
        switch (language) {
            case "java":
                return new JavaCodeSandBox();
            case "python":
                return new PythonCodeSandBox();
            case "example":
            default:
                return new ExampleCodeSandBox();
        }
    }

}
