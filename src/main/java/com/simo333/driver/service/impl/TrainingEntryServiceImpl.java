package com.simo333.driver.service.impl;

import com.simo333.driver.model.Advice;
import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.model.TrainingEntry;
import com.simo333.driver.payload.training.TrainingResultRequest;
import com.simo333.driver.payload.training.TrainingResultResponse;
import com.simo333.driver.repository.TrainingEntryRepository;
import com.simo333.driver.service.AdviceService;
import com.simo333.driver.service.AnswerService;
import com.simo333.driver.service.TrainingEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TrainingEntryServiceImpl implements TrainingEntryService {

    private final TrainingEntryRepository repository;
    private final AdviceService adviceService;
    private final AnswerService answerService;

    @Transactional
    @Override
    public TrainingEntry conductNewTraining(Long adviceId) {
        Advice advice = adviceService.findOne(adviceId);
        Set<Question> questions = Set.copyOf(advice.getQuestions());
        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for advice id " + adviceId);
        }

        TrainingEntry entry = new TrainingEntry(null, questions, null);
        return repository.save(entry);
    }

    @Transactional
    @Override
    public TrainingResultResponse verifyTraining(TrainingResultRequest request) {
        TrainingEntry trainingEntry = repository.findById(request.getTrainingId()).orElseThrow(() -> {
            log.error("Training entry with id '{}' not found", request.getTrainingId());
            return new ResourceNotFoundException(String.format("Training entry with id '%s' not found", request.getTrainingId()));
        });

        TrainingResultResponse response = buildTrainingResponse(trainingEntry, request);
        log.info("Training result: {}", response);
        repository.deleteById(request.getTrainingId());
        //TODO save result for user?
        return response;
    }

    private TrainingResultResponse buildTrainingResponse(TrainingEntry entry, TrainingResultRequest request) {
        List<Answer> answers = request.getAnswers().stream().map(answerService::findById).toList();
        long score = answers.stream().filter(Answer::isCorrect).count();
        int totalPoints = entry.getQuestions().size();
        boolean passed = score == totalPoints;
        return TrainingResultResponse.builder()
                .score(score)
                .totalPoints(totalPoints)
                .passed(passed)
                .build();
    }
}
