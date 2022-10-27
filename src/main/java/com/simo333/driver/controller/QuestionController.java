package com.simo333.driver.controller;

import com.simo333.driver.model.Question;
import com.simo333.driver.payload.question.QuestionRequest;
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
    public Question save(@RequestBody @Valid QuestionRequest request) {
        Question question = questionService.save(request);
        answerService.saveForQuestion(question, request.getAnswers());

        return question;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Question update(@RequestBody @Valid Question question, @PathVariable Long id) {
        question.setId(id);
        return questionService.update(question);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }
}
