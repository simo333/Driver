package com.simo333.driver.service.impl;

import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Quiz;
import com.simo333.driver.payload.quiz.NewQuizResponse;
import com.simo333.driver.payload.quiz.QuizScoreRequest;
import com.simo333.driver.payload.quiz.QuizScoreResponse;
import com.simo333.driver.repository.QuizRepository;
import com.simo333.driver.service.AnswerService;
import com.simo333.driver.service.QuestionService;
import com.simo333.driver.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final QuizRepository repository;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Transactional
    @Override
    public Quiz save(Quiz quiz) {
        return repository.save(quiz);
    }

    @Override
    public Quiz findOne(Long quizId) {
        return repository.findById(quizId).orElseThrow(() -> {
            log.error("Quiz with id '{}' not found", quizId);
            return new ResourceNotFoundException(String.format("Quiz with id '%s' not found", quizId));
        });
    }

    @Transactional
    @Override
    public NewQuizResponse conductNewQuiz() {
        Quiz quiz = new Quiz();
        quiz.setQuestions(questionService.collectRandom());
        Quiz saved = repository.save(quiz);

        return buildNewQuizResponse(saved);
    }

    @Transactional
    @Override
    public QuizScoreResponse verifyQuizScore(QuizScoreRequest request) {
        Quiz quiz = findOne(request.getQuizId());
        quiz.setFinishTimestamp(Instant.now());
        long score = request.getAnswers().stream().filter(answerId -> answerService.findById(answerId).isCorrect()).count();
        int totalPoints = quiz.getQuestions().size();
        long duration = Duration.between(quiz.getBeginTimestamp(), quiz.getFinishTimestamp()).toSeconds();
        return new QuizScoreResponse(score, totalPoints, duration);
    }

    private NewQuizResponse buildNewQuizResponse(Quiz quiz) {
        NewQuizResponse response = new NewQuizResponse();
        response.setQuizId(quiz.getId());
        response.setQuestions(quiz.getQuestions());
        response.setNumberOfQuestions(quiz.getQuestions().size());
        return response;
    }


}
