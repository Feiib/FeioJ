package com.fei.feiojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fei.feiojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.fei.feiojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.fei.feiojbackendmodel.model.entity.QuestionSubmit;
import com.fei.feiojbackendmodel.model.entity.User;
import com.fei.feiojbackendmodel.model.vo.QuestionSubmitVO;


/**
* @author a'u
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2025-08-14 16:52:07
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);

    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
}
