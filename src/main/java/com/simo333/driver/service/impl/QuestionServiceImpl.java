package com.simo333.driver.service.impl;

import com.simo333.driver.exception.InvalidAnswersException;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.answer.AnswerRequest;
import com.simo333.driver.payload.question.QuestionRequest;
import com.simo333.driver.repository.QuestionRepository;
import com.simo333.driver.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository repository;

    @Override
    public Question findOne(Long questionId) {
        return repository.findById(questionId).orElseThrow(() -> {
            log.error("Question with id '{}' not found", questionId);
            return new ResourceNotFoundException(String.format("Question with id '%s' not found", questionId));
        });
    }

    @Override
    public List<Question> findAll() {
        return repository.findAll();
    }


    @Transactional
    @Override
    public Question save(QuestionRequest request) {
        validateOneCorrectAnswer(request);
        log.info("Saving a new question : {}", request.getQuestionText());
        Question question = buildQuestion(request);

        return repository.save(question);
    }

    @Transactional
    @Override
    public Question update(Question question) {
        findOne(question.getId());
        return repository.save(question);
    }

    @Transactional
    @Override
    public void delete(Long questionId) {
        repository.deleteById(questionId);
    }

    public Question buildQuestion(QuestionRequest request) {
        return Question.builder()
                .contents(request.getQuestionText())
                .build();

    }

    public void validateOneCorrectAnswer(QuestionRequest request) {
        long correctAnswers = request.getAnswers().stream().filter(AnswerRequest::getIsCorrect).count();
        log.info(request.toString());
        if (correctAnswers != 1) {
            log.error("Question has to have exactly one correct answer. Found: {}", correctAnswers);
            throw new InvalidAnswersException("Question has to have exactly one correct answer. Found: " + correctAnswers);
        }
    }
}
