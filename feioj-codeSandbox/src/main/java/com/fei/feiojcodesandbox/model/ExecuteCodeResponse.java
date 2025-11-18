package com.fei.feiojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {
    //输出结果
    private List<String> outputList;
    //执行信息
    private JudgeInfo judgeInfo;
    //执行状态
    private Integer status;
    //接口信息
    private String message;
}
