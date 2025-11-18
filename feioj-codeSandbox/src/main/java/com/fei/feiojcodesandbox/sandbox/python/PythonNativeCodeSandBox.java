package com.fei.feiojcodesandbox.sandbox.python;

import com.fei.feiojcodesandbox.model.ExecuteCodeRequest;
import com.fei.feiojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PythonNativeCodeSandBox extends PythonCodeSandBoxTemplate {
    
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest request) {
        return super.execute(request);
    }
    
    public static void main(String[] args) {
        PythonNativeCodeSandBox pythonNativeCodeSandBox = new PythonNativeCodeSandBox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("3 4", "5 6"));
        
        // 测试Python代码
        String pythonCode = "import os\n" +
                "import sys\n" +
                "\n" +
                "def main():\n" +
                "    print(\"Trying to access system...\")\n" +
                "    \n" +
                "    # 尝试使用os模块（在黑名单中）\n" +
                "    current_dir = os.getcwd()\n" +
                "    print(f\"Current directory: {current_dir}\")\n" +
                "    \n" +
                "    # 尝试使用sys模块（在黑名单中）\n" +
                "    print(f\"Python version: {sys.version}\")\n" +
                "\n" +
                "if __name__ == \"__main__\":\n" +
                "    main()";
        
        executeCodeRequest.setCode(pythonCode);
        executeCodeRequest.setLanguage("python");
        ExecuteCodeResponse executeCodeResponse = pythonNativeCodeSandBox.execute(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
}