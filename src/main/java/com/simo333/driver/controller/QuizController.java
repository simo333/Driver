package com.simo333.driver.controller;

import com.simo333.driver.payload.quiz.NewQuizResponse;
import com.simo333.driver.payload.quiz.QuizScoreRequest;
import com.simo333.driver.payload.quiz.QuizScoreResponse;
import com.simo333.driver.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService service;

    @GetMapping("/new")
    @ResponseStatus(OK)
    public NewQuizResponse getNewQuiz() {
        return service.conductNewQuiz();
    }

    @PostMapping("/check-score")
    @ResponseStatus(OK)
    public QuizScoreResponse checkQuizScore(@RequestBody @Valid QuizScoreRequest request) {
        return service.verifyQuizScore(request);
    }
}
