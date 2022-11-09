package com.simo333.driver.service;

import com.simo333.driver.model.Quiz;
import com.simo333.driver.payload.quiz.NewQuizResponse;
import com.simo333.driver.payload.quiz.QuizScoreRequest;
import com.simo333.driver.payload.quiz.QuizScoreResponse;

public interface QuizService {

    Quiz save(Quiz quiz);

    Quiz findOne(Long quizId);

    NewQuizResponse conductNewQuiz();

    QuizScoreResponse verifyQuizScore(QuizScoreRequest request);
}
