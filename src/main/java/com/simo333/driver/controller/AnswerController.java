package com.simo333.driver.controller;

import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.model.Tag;
import com.simo333.driver.service.AnswerService;
import com.simo333.driver.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService service;
    private final QuestionService questionService;

    @GetMapping("/questions/{questionId}/answers")
    @ResponseStatus(HttpStatus.OK)
    List<Answer> findAnswersByQuestionId(@PathVariable Long questionId) {
        return service.findAllByQuestionId(questionId);
    }

    @PostMapping("questions/{questionId}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public Answer save(@RequestBody @Valid Answer answer, @PathVariable Long questionId) {
        Question question = questionService.findOne(questionId);
        answer.setQuestion(question);
        return service.save(answer);
    }


}
