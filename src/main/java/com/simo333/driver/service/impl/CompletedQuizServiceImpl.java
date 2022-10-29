package com.simo333.driver.service.impl;

import com.simo333.driver.model.Advice;
import com.simo333.driver.model.Answer;
import com.simo333.driver.model.CompletedQuiz;
import com.simo333.driver.model.User;
import com.simo333.driver.payload.completed_quiz.CompletedQuizCreateRequest;
import com.simo333.driver.payload.completed_quiz.CompletedQuizResponse;
import com.simo333.driver.repository.CompletedQuizRepository;
import com.simo333.driver.service.AdviceService;
import com.simo333.driver.service.AnswerService;
import com.simo333.driver.service.CompletedQuizService;
import com.simo333.driver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompletedQuizServiceImpl implements CompletedQuizService {

    private final CompletedQuizRepository repository;
    private final UserService userService;
    private final AdviceService adviceService;
    private final AnswerService answerService;

    @Override
    public Page<CompletedQuizResponse> getQuizzesByUser(Long userId, Pageable page) {
        return repository.findAllByUserId(userId, page);
    }

    @Override
    public Page<CompletedQuizResponse> getQuizzesByAdvice(Long adviceId, Pageable page) {
        return repository.findAllByAdviceId(adviceId, page);
    }

    @Override
    public List<CompletedQuizResponse> getQuizzesByUserAndAdvice(Long userId, Long adviceId) {
        return repository.findAllByUserIdAndAdviceId(userId, adviceId);
    }

    @Override
    public CompletedQuizResponse getHighestScoreQuizByUserAndAdvice(Long userId, Long adviceId) {
        return repository.findTopByUserIdAndAdviceId(userId, adviceId).orElseThrow(() -> {
            log.error("Completed Quiz for User id '{}' and Advice id '{}' not found", userId, adviceId);
            return new ResourceNotFoundException(
                    String.format("Completed Quiz for User id '%s' and Advice id '%s' not found", userId, adviceId));
        });
    }

    @Transactional
    @Override
    public CompletedQuiz save(CompletedQuizCreateRequest request) {
        log.info("Saving a new CompletedQuiz.");
        return repository.save(buildCompletedQuiz(request));
    }

    @Transactional
    @Override
    public void delete(Long quizId) {
        log.info("Deleting CompletedQuiz '{}'", quizId);
        repository.deleteById(quizId);
    }

    private CompletedQuiz buildCompletedQuiz(CompletedQuizCreateRequest request) {
        User user = userService.findOne(request.getUserId());
        Advice advice = adviceService.findOne(request.getAdviceId());
        List<Answer> answers = new ArrayList<>();
        request.getAnswers().forEach(answerId -> answers.add(answerService.findById(answerId)));
        int score = (int) answers.stream().filter(Answer::isCorrect).count();
        return CompletedQuiz.builder()
                .user(user)
                .advice(advice)
                .givenAnswers(answers)
                .score(score)
                .build();
    }
}
