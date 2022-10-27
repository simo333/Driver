package com.simo333.driver.service.impl;

import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.answer.AnswerRequest;
import com.simo333.driver.repository.AnswerRepository;
import com.simo333.driver.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AnswerServiceImpl implements AnswerService {

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
    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    @Transactional
    @Override
    public void saveForQuestion(Question question, List<AnswerRequest> answerRequests) {
        log.info("Creating {} Answers for Question {}.", answerRequests.size(), question.getId());

        List<Answer> answers = answerRequests
                .stream()
                .map(answer -> buildAnswerForQuestion(question, answer))
                .toList();

        answerRepository.saveAll(answers);
    }

    @Transactional
    @Override
    public Answer update(Answer answer) {
        findById(answer.getId());
        return answerRepository.save(answer);
    }

    @Transactional
    @Override
    public void delete(Long answerId) {
        answerRepository.deleteById(answerId);
    }

    private Answer buildAnswerForQuestion(Question question, AnswerRequest request) {
        return Answer.builder()
                .contents(request.getText())
                .isCorrect(request.getIsCorrect())
                .question(question)
                .build();
    }
}
