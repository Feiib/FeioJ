package com.fei.feiojcodesandbox.utils;

import com.fei.feiojcodesandbox.model.ExecuteMessage;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 执行进程并获取信息
 */
public class ProcessUtils {
    public static ExecuteMessage getExecuteMessage(Process process, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            //程序执行后得到错误码
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int exitValue = process.waitFor();

            // 读取标准输出
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            ArrayList<String> outputList = new ArrayList<>();
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                outputList.add(readLine);
            }
            String output = StringUtils.join(outputList, "\n");

            // 读取错误输出
            BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            ArrayList<String> errorOutputList = new ArrayList<>();
            String errorReadLine;
            while ((errorReadLine = errorBufferedReader.readLine()) != null) {
                errorOutputList.add(errorReadLine);
            }
            String errorOutput = StringUtils.join(errorOutputList, "\n");

            // 检查是否包含安全错误信息
            boolean hasSecurityError = output.contains("Security error:") ||
                    output.contains("SecurityException") ||
                    errorOutput.contains("Security error:") ||
                    errorOutput.contains("SecurityException") ||
                    errorOutput.contains("error") ||
                    errorOutput.contains("Error") ||
                    errorOutput.contains("Exception") ||
                    output.contains("Exception") ||
                    output.contains("Error") ||
                    output.contains("error");

            if (exitValue == 0 && !hasSecurityError) {
                // 正常退出且无安全错误
                executeMessage.setExitValue(0);
                executeMessage.setMessage(output);
                executeMessage.setError(StringUtils.isNotBlank(errorOutput) ? errorOutput : null);
                System.out.println(opName + "成功");
            } else if (hasSecurityError) {
                // 检测到安全错误，视为执行失败
                executeMessage.setExitValue(1);
                executeMessage.setMessage(""); // 清空正常输出
                executeMessage.setError(output + errorOutput); // 将安全错误信息放到error字段
                System.out.println(opName + "失败");
            } else {
                // 其他异常退出情况
                executeMessage.setExitValue(exitValue);
                executeMessage.setMessage(output);
                executeMessage.setError(StringUtils.isNotBlank(errorOutput) ? errorOutput : null);
                System.out.println(opName + "失败, 错误码: " + exitValue);
            }

            stopWatch.stop();
            executeMessage.setTime(stopWatch.getTotalTimeMillis());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return executeMessage;
    }
}
