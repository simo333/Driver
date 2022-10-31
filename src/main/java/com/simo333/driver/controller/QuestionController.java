package com.simo333.driver.controller;

import com.simo333.driver.model.Question;
import com.simo333.driver.payload.question.QuestionCreateRequest;
import com.simo333.driver.payload.question.QuestionUpdateRequest;
import com.simo333.driver.service.AnswerService;
import com.simo333.driver.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @GetMapping
    @ResponseStatus(OK)
    public Page<Question> getAllQuestions(Pageable page) {
        return questionService.findAll(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Question getById(@PathVariable Long id) {
        return questionService.findOne(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Question save(@RequestBody @Valid QuestionCreateRequest request) {
        Question question = questionService.save(request);
        answerService.saveForQuestion(question, request.getAnswers());

        return question;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public Question update(@RequestBody @Valid QuestionUpdateRequest request, @PathVariable Long id) {
        return questionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }
}
