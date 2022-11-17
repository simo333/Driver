package com.simo333.driver.service.impl;

import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.answer.AnswerCreateRequest;
import com.simo333.driver.payload.answer.AnswerUpdateRequest;
import com.simo333.driver.repository.AnswerRepository;
import com.simo333.driver.repository.QuestionRepository;
import com.simo333.driver.service.AnswerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class AnswerServiceImplTest {

    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void shouldFindAnswer_byGivenId() {
        // given
        String contents = "answer";
        boolean correct = true;
        Answer answer = new Answer();
        answer.setCorrect(correct);
        answer.setContents(contents);

        // when
        answerRepository.save(answer);
        Answer actualAnswer = answerService.findById(answer.getId());

        // then
        assertThat(actualAnswer).isEqualTo(answer);
    }

    @Test
    void shouldThrowException_whenAnswerNotFind() {
        // given
        Long badId = -1L;

        // then
        assertThatThrownBy(() -> answerService.findById(badId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Answer with id '%s' not found", badId));
    }

    @Test
    void shouldFindAnswers_byGivenQuestionId() {
        // given
        Question question = new Question();
        question.setContents("question");
        Answer expectedAnswer = new Answer();
        expectedAnswer.setCorrect(true);
        expectedAnswer.setContents("answer");
        expectedAnswer.setQuestion(question);
        Answer otherAnswer = new Answer();
        otherAnswer.setCorrect(true);
        otherAnswer.setContents("answer2");

        // when
        questionRepository.save(question);
        answerRepository.save(expectedAnswer);
        answerRepository.save(otherAnswer);
        List<Answer> actualAnswers = answerService.findAllByQuestionId(question.getId());

        // then
        assertThat(actualAnswers)
                .hasSize(1)
                .contains(expectedAnswer)
                .doesNotContain(otherAnswer);
    }

    @Test
    void shouldReturnEmptyList_whenAnswerNotFindByQuestionId() {
        // given
        Long badId = -1L;

        // when
        List<Answer> actualAnswers = answerService.findAllByQuestionId(badId);

        // then
        assertThat(actualAnswers).isEmpty();
    }

    @Test
    void shouldReturnCorrectList() {
        // given
        AnswerCreateRequest request1 = new AnswerCreateRequest();
        request1.setText("answer1");
        request1.setIsCorrect(true);
        AnswerCreateRequest request2 = new AnswerCreateRequest();
        request2.setText("answer2");
        request2.setIsCorrect(false);
        List<AnswerCreateRequest> answers = new ArrayList<>();
        answers.add(request1);
        answers.add(request2);
        Question question = new Question();
        question.setContents("question");

        // when
        questionRepository.save(question);
        answerService.saveForQuestion(question, answers);
        List<Answer> actualAnswers = answerService.findAllByQuestionId(question.getId());

        // then
        assertThat(actualAnswers).hasSize(2);
    }

    @Test
    void shouldUpdateAnswer() {
        // given
        String newContents = "new";
        Answer answer = new Answer();
        answer.setContents("some");
        answer.setCorrect(true);
        AnswerUpdateRequest updateRequest = new AnswerUpdateRequest();
        updateRequest.setText(newContents);

        // when
        answerRepository.save(answer);
        Answer updated = answerService.update(answer.getId(), updateRequest);

        // then
        assertThat(updated.getContents()).isEqualTo(newContents);
    }

    @Test
    void shouldBeEmpty_whenDeleted() {
        // given
        Answer answer = new Answer();
        answer.setContents("some");
        answer.setCorrect(true);

        // when
        answerRepository.save(answer);
        answerService.delete(answer.getId());
        Optional<Answer> actualAnswer = answerRepository.findById(answer.getId());

        // then
        assertThat(actualAnswer).isEmpty();

    }
}