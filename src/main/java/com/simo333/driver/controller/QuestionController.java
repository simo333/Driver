package com.simo333.driver.controller;

import com.simo333.driver.model.Question;
import com.simo333.driver.payload.question.QuestionCreateRequest;
import com.simo333.driver.payload.question.QuestionUpdateRequest;
import com.simo333.driver.service.AnswerService;
import com.simo333.driver.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Question> getAllQuestions() {
        return questionService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Question save(@RequestBody @Valid QuestionCreateRequest request) {
        Question question = questionService.save(request);
        answerService.saveForQuestion(question, request.getAnswers());

        return question;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Question update(@RequestBody @Valid QuestionUpdateRequest request, @PathVariable Long id) {
        return questionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }
}
