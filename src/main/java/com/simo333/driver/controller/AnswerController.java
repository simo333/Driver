package com.simo333.driver.controller;

import com.simo333.driver.model.Answer;
import com.simo333.driver.payload.answer.AnswerUpdateRequest;
import com.simo333.driver.service.AnswerService;
import com.simo333.driver.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService service;

    @GetMapping("/questions/{questionId}/answers")
    @ResponseStatus(OK)
    List<Answer> findAnswersByQuestionId(@PathVariable Long questionId) {
        return service.findAllByQuestionId(questionId);
    }

    @PutMapping("/answers/{answerId}")
    @ResponseStatus(OK)
    public Answer update(@RequestBody @Valid AnswerUpdateRequest request, @PathVariable Long answerId) {
        return service.update(answerId, request);
    }

    @DeleteMapping("/answers/{answerId}")
    @ResponseStatus(OK)
    public void delete(@PathVariable Long answerId) {
        service.delete(answerId);
    }

}
