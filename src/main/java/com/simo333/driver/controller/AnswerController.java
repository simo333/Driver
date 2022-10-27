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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService service;

    @GetMapping("/questions/{questionId}/answers")
    @ResponseStatus(HttpStatus.OK)
    List<Answer> findAnswersByQuestionId(@PathVariable Long questionId) {
        return service.findAllByQuestionId(questionId);
    }

    @PutMapping("answers/{answerId}")
    @ResponseStatus(HttpStatus.OK)
    public Answer update(@RequestBody @Valid AnswerUpdateRequest request, @PathVariable Long answerId) {
        return service.update(answerId, request);
    }

    @DeleteMapping("/answers/{answerId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long answerId) {
        service.delete(answerId);
    }

}
