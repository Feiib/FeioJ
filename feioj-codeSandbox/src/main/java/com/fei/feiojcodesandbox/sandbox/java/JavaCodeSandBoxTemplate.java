package com.fei.feiojcodesandbox.sandbox.java;

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
 * Java代码沙箱模板
 * 继承基类，实现Java特定的编译和执行逻辑
 */
@Slf4j
public class JavaCodeSandBoxTemplate extends BaseCodeSandBoxTemplate {

    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    // 安全管理器相关常量
    private static final String SECURITY_MANAGER_CLASS = "com.fei.feiojcodesandbox.security.DefaultSecurityManager";
    private static final String SANDBOX_ALLOWED_DIR_PROP = "sandbox.allowedDir";
    private static final String SANDBOX_APP_CLASSES_DIR_PROP = "sandbox.appClassesDir";
    private static final String JAVA_SECURITY_MANAGER_PROP = "java.security.manager";

    // 内存和编码配置常量
    private static final String MEMORY_LIMIT = "-Xmx256m";
    private static final String FILE_ENCODING = "-Dfile.encoding=UTF-8";
    private static final String TARGET_CLASSES_DIR = "target/classes";

    private static final List<String> blackList = Arrays.asList("Files", "exec");

    private static final List<String> JAVA_BLACKLIST = Arrays.asList(
            "java.nio.file", "java.net", "java.lang.reflect",
            "java.lang.Runtime", "java.lang.ProcessBuilder",
            "java.util.concurrent", "java.lang.Thread",
            "java.lang.ClassLoader", "System.exit"
    );

    private static final WordTree JAVA_WORD_TREE;

    static {
        // 初始化字典树
        JAVA_WORD_TREE = new WordTree();
        JAVA_WORD_TREE.addWords(JAVA_BLACKLIST);
    }


    @Override
    protected String getCodeFileName() {
        return GLOBAL_JAVA_CLASS_NAME;
    }

    @Override
    protected ExecuteMessage compileOrCheck(File userCodeFile) {
        // 先进行黑名单检查
        ExecuteMessage blacklistCheckMsg = checkBlacklist(userCodeFile);
        if (blacklistCheckMsg.getExitValue() != 0) {
            return blacklistCheckMsg;
        }
        return compileCode(userCodeFile);
    }

    @Override
    protected String getRunCommand(File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        return String.format("java %s %s -cp %s Main", MEMORY_LIMIT, FILE_ENCODING, userCodeParentPath);
    }

/*        @Override
        protected String getRunCommand(File userCodeFile) {
            String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
            // 应用classes目录（使安全管理器类可加载）
            String appClassesPath = new File(System.getProperty("user.dir"), TARGET_CLASSES_DIR).getAbsolutePath();
            // 组合classpath，Windows下使用分号分隔

            String classPath = String.format("\"%s\":\"%s\"", userCodeParentPath, appClassesPath);
            // 注入安全管理器以及允许目录到子进程
            String securityProps = String.format("-D%s=%s -D%s=\"%s\" -D%s=\"%s\"",
                JAVA_SECURITY_MANAGER_PROP, SECURITY_MANAGER_CLASS,
                SANDBOX_ALLOWED_DIR_PROP, userCodeParentPath,
                SANDBOX_APP_CLASSES_DIR_PROP, appClassesPath);
            return String.format("java %s %s %s -cp %s Main", MEMORY_LIMIT, FILE_ENCODING, securityProps, classPath);
        }*/

    @Override
    protected String getCompileErrorMessage() {
        return "Java编译错误";
    }

    @Override
    protected String getLanguageName() {
        return "Java";
    }

    @Override
    protected boolean needsNewLine() {
        return false; // Java程序通常不需要额外的换行符
    }

    /**
     * 编译代码
     *
     * @param userCodeFile 用户代码文件
     * @return 编译消息
     */
    public ExecuteMessage compileCode(File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage compileMsg = ProcessUtils.getExecuteMessage(compileProcess, "编译");
            return compileMsg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 检查Java代码中的黑名单模块
     *
     * @param userCodeFile 用户代码文件
     * @return 黑名单检查消息
     */
    public ExecuteMessage checkBlacklist(File userCodeFile) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            String code = FileUtil.readString(userCodeFile, StandardCharsets.UTF_8);
            List<String> foundWords = JAVA_WORD_TREE.matchAll(code);
            if (!foundWords.isEmpty()) {
                executeMessage.setExitValue(1);
                executeMessage.setError("代码中包含禁用的模块或函数: " + String.join(", ", foundWords));
                executeMessage.setMessage("黑名单检查失败");
                log.warn("java代码包含黑名单内容: {}", foundWords);
            } else {
                executeMessage.setExitValue(0);
                log.info("java黑名单检查通过");
            }
        } catch (Exception e) {
            executeMessage.setExitValue(1);
            executeMessage.setError("黑名单检查异常: " + e.getMessage());
            executeMessage.setMessage("黑名单检查失败");
            log.error("java黑名单检查异常", e);
        }
        return executeMessage;
    }
}
