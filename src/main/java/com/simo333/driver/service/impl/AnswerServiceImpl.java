package com.simo333.driver.service.impl;

import com.simo333.driver.exception.IllegalQuestionStateException;
import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.answer.AnswerCreateRequest;
import com.simo333.driver.payload.answer.AnswerUpdateRequest;
import com.simo333.driver.repository.AnswerRepository;
import com.simo333.driver.service.AnswerService;
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
public class AnswerServiceImpl implements AnswerService {
    private static final int MINIMAL_ANSWERS_AMOUNT_PER_QUESTION = 2;

    private final AnswerRepository answerRepository;

    @Override
    public Answer findById(Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(() -> {
            log.error("Answer with id '{}' not found", answerId);
            return new ResourceNotFoundException(String.format("Answer with id '%s' not found", answerId));
        });
    }

    @Override
    public List<Answer> findAllByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }


    @Transactional
    @Override
    public void saveForQuestion(Question question, List<AnswerCreateRequest> answerCreateRequests) {
        log.info("Creating {} Answers for Question {}.", answerCreateRequests.size(), question.getId());

        List<Answer> answers = answerCreateRequests
                .stream()
                .map(answer -> buildAnswerForQuestion(question, answer))
                .toList();

        answerRepository.saveAll(answers);
    }

    @Transactional
    @Override
    public Answer update(Long answerId, AnswerUpdateRequest request) {
        Answer answer = findById(answerId);
        answer.setContents(request.getText());
        log.info("Updating answer. After changers: {}", answer);
        return answerRepository.save(answer);
    }

    @Transactional
    @Override
    public void delete(Long answerId) {
        Answer answer = findById(answerId);
        if (answerRepository.countByQuestionId(answer.getQuestion().getId()) == MINIMAL_ANSWERS_AMOUNT_PER_QUESTION) {
            throw new IllegalQuestionStateException("Cannot delete. Question must have at least 2 answers.");
        }
        if(answer.isCorrect()) {
            throw new IllegalQuestionStateException(
                    String.format("Cannot delete Answer '%s'. Question must have a correct answer.", answerId));
        }
        answerRepository.deleteById(answerId);
    }

    private Answer buildAnswerForQuestion(Question question, AnswerCreateRequest request) {
        return Answer.builder()
                .contents(request.getText())
                .isCorrect(request.getIsCorrect())
                .question(question)
                .build();
    }
}
