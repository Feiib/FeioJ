package com.fei.feiojbackendjudgeservice.judge.strategy.answer;

import java.util.*;

/**
 * 忽略顺序
 */


public class UnorderedJudgeStrategy implements AnswerJudgeStrategy {
    @Override
    public boolean judge(String expected, String actual) {
        List<String> expectedLines = normalize(expected);
        List<String> actualLines = normalize(actual);
        return expectedLines.equals(actualLines);
    }

    private List<String> normalize(String input) {
        // 把存储的 "\n" 转换成真正的换行符
        input = input.replace("\\n", "\n");

        List<String> result = new ArrayList<>();
        String[] lines = input.trim().split("\\r?\\n"); // 按行切分
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            Arrays.sort(parts); // 对单行内部排序
            result.add(String.join(" ", parts));
        }
        Collections.sort(result); // 对所有行排序
        return result;
    }


    public static void main(String[] args) {
        String expected = "-1 0 1\n2 -1 -1";
        String actual   = "-1 0 1\n2 -1 -1";

        UnorderedJudgeStrategy judge = new UnorderedJudgeStrategy();
        System.out.println(judge.judge(expected, actual));
    }
}