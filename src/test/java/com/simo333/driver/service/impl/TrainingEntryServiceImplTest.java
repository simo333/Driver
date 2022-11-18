package com.simo333.driver.service.impl;

import com.simo333.driver.model.Advice;
import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.model.TrainingEntry;
import com.simo333.driver.payload.training.TrainingResultRequest;
import com.simo333.driver.payload.training.TrainingResultResponse;
import com.simo333.driver.repository.AdviceRepository;
import com.simo333.driver.repository.AnswerRepository;
import com.simo333.driver.repository.QuestionRepository;
import com.simo333.driver.service.TrainingEntryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class TrainingEntryServiceImplTest {

    @Autowired
    private TrainingEntryService service;
    @Autowired
    private AdviceRepository adviceRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void shouldConductNewTraining() {
        // given
        Question question = new Question();
        question.setContents("question");
        Advice advice = new Advice();
        advice.setTitle("title");
        advice.setDescription("description");
        advice.setQuestions(Set.of(question));

        // when
        questionRepository.save(question);
        Advice savedAdvice = adviceRepository.save(advice);
        TrainingEntry actualTraining = service.conductNewTraining(savedAdvice.getId());

        // then
        assertThat(actualTraining.getQuestions()).contains(question);

    }

    @Test
    void shouldThrowException_whenNoQuestionsForTraining() {
        // given
        Advice advice = new Advice();
        advice.setTitle("title2");
        advice.setDescription("description2");

        // when
        adviceRepository.save(advice);

        // then
        assertThatThrownBy(() -> service.conductNewTraining(advice.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No questions found for advice id " + advice.getId());

    }

    @Test
    void shouldVerifyTraining() {
        // given
        Question question = new Question();
        question.setContents("question");

        Advice advice = new Advice();
        advice.setTitle("title3");
        advice.setDescription("description3");
        advice.setQuestions(Set.of(question));

        Answer answer = new Answer();
        answer.setCorrect(true);
        answer.setContents("answer");
        answer.setQuestion(question);
        Answer answer2 = new Answer();
        answer2.setCorrect(false);
        answer2.setContents("answer");
        answer2.setQuestion(question);

        // when
        questionRepository.save(question);
        adviceRepository.save(advice);
        answerRepository.saveAll(List.of(answer, answer2));

        Set<Long> answers = new HashSet<>();
        answers.add(answer.getId());
        answers.add(answer2.getId());

        TrainingResultRequest request = new TrainingResultRequest();
        request.setAnswers(answers);

        TrainingEntry trainingEntry = service.conductNewTraining(advice.getId());
        request.setTrainingId(trainingEntry.getId());
        request.setAnswers(answers);
        TrainingResultResponse response = service.verifyTraining(request);

        // then
        assertThat(response.getScore()).isEqualTo(1);
        assertThat(response.getTotalPoints()).isEqualTo(1);

    }
}