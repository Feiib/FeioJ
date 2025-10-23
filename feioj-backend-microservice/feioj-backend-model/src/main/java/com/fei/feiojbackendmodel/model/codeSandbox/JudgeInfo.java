package com.fei.feiojbackendmodel.model.codeSandbox;

import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import lombok.Data;

/**
 * 判题信息
 */
@Data
public class JudgeInfo {
    /**
     * 语言
     */
    private String language;
    /**
     * 程序执行状态
     */
    private String status;
    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存
     */
    private Long memory;

    /**
     * 消耗时间（KB）
     */
    private Long time;
}
