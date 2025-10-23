package com.fei.feiojbackendjudgeservice.judge;


import com.fei.feiojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
