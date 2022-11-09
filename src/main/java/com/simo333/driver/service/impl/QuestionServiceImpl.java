package com.simo333.driver.service.impl;

import com.simo333.driver.exception.IllegalQuestionStateException;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.answer.AnswerCreateRequest;
import com.simo333.driver.payload.question.QuestionCreateRequest;
import com.simo333.driver.payload.question.QuestionUpdateRequest;
import com.simo333.driver.repository.QuestionRepository;
import com.simo333.driver.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private static final int NUMBER_OF_QUIZ_QUESTIONS = 20;
    private static final int NUMBER_OF_CORRECT_ANSWERS = 1;
    private final QuestionRepository repository;

    @Override
    public Question findOne(Long questionId) {
        return repository.findById(questionId).orElseThrow(() -> {
            log.error("Question with id '{}' not found", questionId);
            return new ResourceNotFoundException(String.format("Question with id '%s' not found", questionId));
        });
    }

    @Override
    public Page<Question> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Set<Question> collectRandom() {
        Set<Question> questions = repository.findRandom(NUMBER_OF_QUIZ_QUESTIONS);
        if (questions.size() < NUMBER_OF_QUIZ_QUESTIONS) {
            throw new IllegalQuestionStateException
                    ("Not enough questions to conduct a quiz. Minimal is " + NUMBER_OF_QUIZ_QUESTIONS);
        }
        return questions;
    }

    @Transactional
    @Override
    public Question save(QuestionCreateRequest request) {
        validateOneCorrectAnswer(request);
        log.info("Saving a new question : {}", request.getQuestionText());
        Question question = buildQuestion(request);
        return repository.save(question);
    }

    @Transactional
    @Override
    public Question update(Long questionId, QuestionUpdateRequest request) {
        Question question = findOne(questionId);
        log.info("Updating question. After changes: {}", question);
        return repository.save(question);
    }

    @Transactional
    @Override
    public void delete(Long questionId) {
        log.info("Deleting Question '{}'", questionId);
        repository.deleteById(questionId);
    }

    private Question buildQuestion(QuestionCreateRequest request) {
        return Question.builder()
                .contents(request.getQuestionText())
                .build();

    }

    public void validateOneCorrectAnswer(QuestionCreateRequest request) {
        long correctAnswers = request.getAnswers().stream().filter(AnswerCreateRequest::getIsCorrect).count();
        log.info(request.toString());
        if (correctAnswers != NUMBER_OF_CORRECT_ANSWERS) {
            log.error("Question has to have exactly one correct answer. Found: {}", correctAnswers);
            throw new IllegalQuestionStateException(
                    "Question has to have exactly one correct answer. Found: " + correctAnswers);
        }
    }
}
