package com.fei.feiojcodesandbox.sandbox.python;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.WordTree;
import com.fei.feiojcodesandbox.model.ExecuteMessage;
import com.fei.feiojcodesandbox.sandbox.base.BaseCodeSandBoxTemplate;
import com.fei.feiojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Python代码沙箱模板
 * 继承基类，实现Python特定的语法检查和执行逻辑
 */
@Slf4j
public class PythonCodeSandBoxTemplate extends BaseCodeSandBoxTemplate {
    
    public static final String GLOBAL_PYTHON_FILE_NAME = "main.py";
    
    // Python黑名单模块
    private static final List<String> PYTHON_BLACKLIST = Arrays.asList(
            "os", "subprocess", "socket", "urllib", "requests",
            "http", "ftplib", "smtplib", "telnetlib", "webbrowser",
            "pickle", "marshal", "shelve", "dbm", "sqlite3",
            "threading", "multiprocessing", "asyncio", "concurrent",
            "ctypes", "mmap", "tempfile", "shutil", "glob",
            "importlib", "__import__", "eval", "exec", "compile",
            "open", "file", "input.__globals__", "__builtins__.__import__"
    );


    private static final WordTree PYTHON_WORD_TREE;
    
    static {
        // 初始化Python黑名单字典树
        PYTHON_WORD_TREE = new WordTree();
        PYTHON_WORD_TREE.addWords(PYTHON_BLACKLIST);
    }
    @Override
    protected String getCodeFileName() {
        return GLOBAL_PYTHON_FILE_NAME;
    }

    @Override
    protected ExecuteMessage compileOrCheck(File userCodeFile) {
        // 先进行黑名单检查
        ExecuteMessage blacklistCheckMsg = checkBlacklist(userCodeFile);
        if (blacklistCheckMsg.getExitValue() != 0) {
            return blacklistCheckMsg;
        }
        // 然后进行语法检查
        return checkSyntax(userCodeFile);
    }

    @Override
    protected String getRunCommand(File userCodeFile) {
        String allowedDir = userCodeFile.getParentFile().getAbsolutePath();
        // Python执行命令，直接运行Python文件
        return String.format("python3 %s", userCodeFile.getAbsolutePath());
    }

    @Override
    protected String getCompileErrorMessage() {
        return "Python语法错误";
    }

    @Override
    protected String getLanguageName() {
        return "Python";
    }

    @Override
    protected boolean needsNewLine() {
        return true; // Python程序需要换行符
    }

    /**
     * 检查Python代码中的黑名单模块
     *
     * @param userCodeFile 用户代码文件
     * @return 黑名单检查消息
     */
    public ExecuteMessage checkBlacklist(File userCodeFile) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            String code = FileUtil.readString(userCodeFile, StandardCharsets.UTF_8);
            List<String> foundWords = PYTHON_WORD_TREE.matchAll(code);
            if (!foundWords.isEmpty()) {
                executeMessage.setExitValue(1);
                executeMessage.setError("代码中包含禁用的模块或函数: " + String.join(", ", foundWords));
                executeMessage.setMessage("黑名单检查失败");
                log.warn("Python代码包含黑名单内容: {}", foundWords);
            } else {
                executeMessage.setExitValue(0);
                executeMessage.setMessage("黑名单检查通过");
            }
        } catch (Exception e) {
            executeMessage.setExitValue(1);
            executeMessage.setError("黑名单检查异常: " + e.getMessage());
            executeMessage.setMessage("黑名单检查失败");
            log.error("Python黑名单检查异常", e);
        }
        return executeMessage;
    }

    /**
     * 检查Python代码语法
     *
     * @param userCodeFile 用户代码文件
     * @return 语法检查消息
     */
    private ExecuteMessage checkSyntax(File userCodeFile) {
        
        // 使用python -m py_compile检查语法
        List<String> command = Arrays.asList("python3", "-m", "py_compile", userCodeFile.getAbsolutePath());
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            ExecuteMessage syntaxMsg = ProcessUtils.getExecuteMessage(process, "语法检查");
//            Process syntaxCheckProcess = Runtime.getRuntime().exec(syntaxCheckCmd);
//            ExecuteMessage syntaxMsg = ProcessUtils.getExecuteMessage(syntaxCheckProcess, "语法检查");
            return syntaxMsg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}