package com.fei.feiojcodesandbox.model;

import lombok.Data;

@Data
public class ExecuteMessage {
    private Integer exitValue;
    private String message;
    private String error;
    private long time;
    private long memory;
}
