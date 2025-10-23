package com.fei.feiojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fei.feiojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.fei.feiojbackendmodel.model.entity.Question;
import com.fei.feiojbackendmodel.model.entity.QuestionSubmit;
import com.fei.feiojbackendmodel.model.vo.QuestionVO;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

/**
* @author a'u
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-08-14 16:50:28
*/
@FeignClient(name = "feioj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

}
