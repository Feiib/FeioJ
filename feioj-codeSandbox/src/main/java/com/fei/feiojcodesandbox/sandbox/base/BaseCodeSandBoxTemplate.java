package com.fei.feiojcodesandbox.sandbox.base;

import cn.hutool.core.io.FileUtil;
import com.fei.feiojcodesandbox.model.ExecuteCodeRequest;
import com.fei.feiojcodesandbox.model.ExecuteCodeResponse;
import com.fei.feiojcodesandbox.model.ExecuteMessage;
import com.fei.feiojcodesandbox.model.JudgeInfo;
import com.fei.feiojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 代码沙箱模板基类
 * 提取Java和Python代码沙箱的公共功能
 */
@Slf4j
public abstract class BaseCodeSandBoxTemplate implements CodeSandBox {
    
    // 公共常量
    public static final String GLOBAL_CODE_DIR_NAME = "tempCode";
    public static final long TIME_OUT = 5000;



    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest request) {
        List<String> inputList = request.getInputList();
        String code = request.getCode();
        
        // 保存用户代码到文件
        File userCodeFile = saveCodeToFile(code);
        
        // 编译或语法检查（由子类实现）
        ExecuteMessage compileOrCheckMsg = compileOrCheck(userCodeFile);
        System.out.println(compileOrCheckMsg);
        
        // 如果编译或语法检查失败，直接返回错误
        if (compileOrCheckMsg.getExitValue() != 0) {
            ExecuteCodeResponse errorResponse = new ExecuteCodeResponse();
            errorResponse.setStatus(2);
            // 过滤错误消息中的敏感路径信息
            String filteredError = filterSensitivePaths(compileOrCheckMsg.getError());
            errorResponse.setMessage(getCompileErrorMessage() + ": " + filteredError);
            errorResponse.setOutputList(new ArrayList<>());
            errorResponse.setJudgeInfo(new JudgeInfo());
            return errorResponse;
        }

        // 执行程序
        List<ExecuteMessage> executeMessageList = executeCode(userCodeFile, inputList);
        
        // 收集整理输出
        ExecuteCodeResponse executeCodeResponse = getResponse(executeMessageList);
        
        // 清理文件
        boolean b = clearTempFile(userCodeFile);
        if (!b) {
            log.error("清理文件失败{}", userCodeFile.getAbsoluteFile());
        }
        return executeCodeResponse;
    }

    /**
     * 获取错误响应,遇到异常,直接返回
     *
     * @param e 异常
     * @return 错误响应
     */
    protected ExecuteCodeResponse getErrorResponse(Exception e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setMessage(e.getMessage());
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }

    /**
     * 保存代码到文件
     *
     * @param code 用户提交的代码
     * @return 代码文件
     */
    public File saveCodeToFile(String code) {
        // 新建目录，把每个用户的代码存放在独立的目录下
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        
        // 没有则创建目录
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + getCodeFileName();
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    /**
     * 执行代码
     *
     * @param userCodeFile 用户代码文件
     * @param inputList 输入列表
     * @return 执行消息列表
     */
    public List<ExecuteMessage> executeCode(File userCodeFile, List<String> inputList) {
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        
        for (String input : inputList) {
            if (input.equals("[") || input.equals("]") || input.equals(" ")) {
                continue;
            }
            String runCmd = getRunCommand(userCodeFile);

            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                
                // 写入输入数据
                try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(runProcess.getOutputStream()))) {
                    writer.write(input);
                    if (needsNewLine()) {
                        writer.newLine();
                    }
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException("写入输入用例异常: " + e.getMessage());
                }

                // 超时控制
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println(getLanguageName() + "程序执行超时");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                
                ExecuteMessage runMsg = ProcessUtils.getExecuteMessage(runProcess, "运行");
                System.out.println(runMsg);
                executeMessageList.add(runMsg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return executeMessageList;
    }

    /**
     * 整理执行信息,得到需要的响应信息
     *
     * @param executeMessageList 执行消息列表
     * @return 执行响应
     */
    public ExecuteCodeResponse getResponse(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        
        for (ExecuteMessage executeMessage : executeMessageList) {
            String error = executeMessage.getError();
            // 检查是否有错误信息（包括安全错误）
            if (error != null && !error.trim().isEmpty()) {
                executeCodeResponse.setStatus(3);
                // 如果是安全错误，提供更明确的错误信息
                if (error.contains("Security error:") || error.contains("SecurityException")) {
                    executeCodeResponse.setMessage("安全违规: 代码尝试执行被禁止的操作");
                } else {
                    executeCodeResponse.setMessage("执行错误: " + error);
                }
                break;
            }
            
            // 检查退出码，如果不是0说明执行失败
            if (executeMessage.getExitValue() != 0) {
                executeCodeResponse.setStatus(3);
                executeCodeResponse.setMessage("程序执行失败，退出码: " + executeMessage.getExitValue());
                break;
            }
            
            outputList.add(executeMessage.getMessage());
            long time = executeMessage.getTime();
            if (time > maxTime) {
                maxTime = time;
            }
        }
        
        // 只有所有执行都成功才设置为成功状态
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 清理临时文件
     *
     * @param userCodeFile 用户代码文件
     * @return 是否清理成功
     */
    public boolean clearTempFile(File userCodeFile) {
        boolean result = false;
        if (userCodeFile.getParentFile().exists()) {
            result = FileUtil.del(userCodeFile.getParentFile());
        }
        return result;
    }

    /**
     * 过滤错误消息中的敏感路径信息
     *
     * @param errorMessage 原始错误消息
     * @return 过滤后的错误消息
     */
    private String filterSensitivePaths(String errorMessage) {
        if (errorMessage == null) {
            return null;
        }
        
        // 获取系统用户目录和项目根目录
        String userDir = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");
        
        String filteredMessage = errorMessage;
        
        // 替换完整的绝对路径为相对路径
        if (userDir != null) {
            filteredMessage = filteredMessage.replace(userDir, ".");
        }
        
        // 替换用户主目录路径
        if (userHome != null) {
            filteredMessage = filteredMessage.replace(userHome, "~");
        }
        
        // 替换tempCode目录中的UUID为通用标识
        filteredMessage = filteredMessage.replaceAll(
            "tempCode[/\\\\][a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}",
            "tempCode/[USER_CODE_DIR]"
        );
        
        // 移除Windows驱动器盘符（如D:\）
        filteredMessage = filteredMessage.replaceAll("[A-Z]:[/\\\\]", "");
        
        return filteredMessage;
    }

    // ========== 抽象方法，由子类实现 ==========
    
    /**
     * 获取代码文件名
     * @return 文件名
     */
    protected abstract String getCodeFileName();
    
    /**
     * 语法检查
     * @param userCodeFile 用户代码文件
     * @return 执行消息
     */
    protected abstract ExecuteMessage compileOrCheck(File userCodeFile);
    
    /**
     * 获取运行命令
     * @param userCodeFile 用户代码文件
     * @return 运行命令
     */
    protected abstract String getRunCommand(File userCodeFile);
    
    /**
     * 获取编译错误消息前缀
     * @return 错误消息前缀
     */
    protected abstract String getCompileErrorMessage();
    
    /**
     * 获取语言名称
     * @return 语言名称
     */
    protected abstract String getLanguageName();
    
    /**
     * 是否需要在输入后添加换行符
     * @return 是否需要换行符
     */
    protected abstract boolean needsNewLine();
}