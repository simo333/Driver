package com.simo333.driver.service;

import com.simo333.driver.model.CompletedQuiz;
import com.simo333.driver.payload.completed_quiz.CompletedQuizCreateRequest;
import com.simo333.driver.payload.completed_quiz.CompletedQuizResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompletedQuizService {

    Page<CompletedQuizResponse> getQuizzesByUser(Long userId, Pageable page);

    Page<CompletedQuizResponse> getQuizzesByAdvice(Long adviceId, Pageable page);

    List<CompletedQuizResponse> getQuizzesByUserAndAdvice(Long userId, Long adviceId);

    CompletedQuizResponse getHighestScoreQuizByUserAndAdvice(Long userId, Long adviceId);

    CompletedQuiz save(CompletedQuizCreateRequest request);

    void delete(Long quizId);
}
