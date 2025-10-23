package com.fei.feiojbackendjudgeservice.controller.inner;

import com.fei.feiojbackendjudgeservice.judge.JudgeService;
import com.fei.feiojbackendmodel.model.entity.QuestionSubmit;
import com.fei.feiojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class InnerJudgeController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId){
        return judgeService.doJudge(questionSubmitId);
    }
}
