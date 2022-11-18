package com.simo333.driver.service.impl;

import com.simo333.driver.exception.IllegalQuestionStateException;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.answer.AnswerCreateRequest;
import com.simo333.driver.payload.question.QuestionCreateRequest;
import com.simo333.driver.payload.question.QuestionUpdateRequest;
import com.simo333.driver.repository.QuestionRepository;
import com.simo333.driver.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class QuestionServiceImplTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;

    @BeforeEach
    void cleanDb() {
        questionRepository.deleteAll();
    }

    @Test
    void shouldFindQuestion_whenCorrect() {
        // given
        Question expectedQuestion = new Question();
        expectedQuestion.setContents("question");

        // when
        questionRepository.save(expectedQuestion);
        Question actualQuestion = questionService.findOne(expectedQuestion.getId());

        // then
        assertThat(actualQuestion).isEqualTo(expectedQuestion);

    }

    @Test
    void shouldThrowException_whenNotFind() {
        // given
        Long badId = -1L;

        // then
        assertThatThrownBy(() -> questionService.findOne(badId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Question with id '%s' not found", badId);
    }

    @Test
    void shouldReturnAllQuestions() {
        // given
        Question question = new Question();
        question.setContents("question1");
        Question question2 = new Question();
        question2.setContents("question12");

        // when
        questionRepository.save(question);
        questionRepository.save(question2);
        Page<Question> questions = questionService.findAll(PageRequest.of(0, 20));

        // then
        assertThat(questions)
                .hasSize(2)
                .contains(question, question2);

    }

    @Test
    void shouldCollect20RandomQuestions() {
        // given
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Question question = new Question();
            question.setContents("question" + i);
            questions.add(question);
        }

        // when
        questionRepository.saveAll(questions);
        Set<Question> actualQuestions = questionService.collectRandom();

        // then
        assertThat(actualQuestions)
                .hasSize(20)
                .containsAnyElementsOf(questions);

    }

    @Test
    void shouldThrowException_whenNotEnoughQuestionsInDb() {
        assertThatThrownBy(() -> questionService.collectRandom())
                .isInstanceOf(IllegalQuestionStateException.class)
                .hasMessageContaining("Not enough questions to conduct a quiz.");
    }

    @Test
    void shouldSave() {
        // given
        AnswerCreateRequest answer1 = new AnswerCreateRequest();
        answer1.setText("answer1");
        answer1.setIsCorrect(true);
        AnswerCreateRequest answer2 = new AnswerCreateRequest();
        answer2.setText("answer2");
        answer2.setIsCorrect(false);
        List<AnswerCreateRequest> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);

        QuestionCreateRequest request = new QuestionCreateRequest();
        request.setQuestionText("question1");
        request.setAnswers(answers);

        // when
        Question saved = questionService.save(request);
        Question actualQuestion = questionService.findOne(saved.getId());

        // then
        assertThat(saved).isEqualTo(actualQuestion);
    }

    @Test
    void shouldThrowException_whenNotEnoughCorrectAnswers() {
        // given
        AnswerCreateRequest answer1 = new AnswerCreateRequest();
        answer1.setText("answer1");
        answer1.setIsCorrect(false);
        AnswerCreateRequest answer2 = new AnswerCreateRequest();
        answer2.setText("answer2");
        answer2.setIsCorrect(false);
        List<AnswerCreateRequest> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);

        QuestionCreateRequest request = new QuestionCreateRequest();
        request.setQuestionText("question1");
        request.setAnswers(answers);

        // then
        assertThatThrownBy(() -> questionService.save(request))
                .isInstanceOf(IllegalQuestionStateException.class)
                .hasMessageContaining("Question has to have exactly one correct answer");
    }

    @Test
    void shouldThrowException_whenTooManyCorrectAnswers() {
        // given
        AnswerCreateRequest answer1 = new AnswerCreateRequest();
        answer1.setText("answer1");
        answer1.setIsCorrect(true);
        AnswerCreateRequest answer2 = new AnswerCreateRequest();
        answer2.setText("answer2");
        answer2.setIsCorrect(true);
        List<AnswerCreateRequest> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);

        QuestionCreateRequest request = new QuestionCreateRequest();
        request.setQuestionText("question1");
        request.setAnswers(answers);

        // then
        assertThatThrownBy(() -> questionService.save(request))
                .isInstanceOf(IllegalQuestionStateException.class)
                .hasMessageContaining("Question has to have exactly one correct answer");
    }

    @Test
    void shouldUpdateQuestion() {
        // given
        String updatedQuestion = "New Question";
        Question question = new Question();
        question.setContents("question");
        QuestionUpdateRequest request = new QuestionUpdateRequest();
        request.setQuestionText(updatedQuestion);
        // when
        Question saved = questionRepository.save(question);
        Question updated = questionService.update(saved.getId(), request);

        // then
        assertThat(updated.getContents()).isEqualTo(updatedQuestion);

    }

    @Test
    void shouldDelete() {
        // given
        Question question = new Question();
        question.setContents("question");

        // when
        Question saved = questionRepository.save(question);
        questionService.delete(saved.getId());
        Optional<Question> actualQuestion = questionRepository.findById(saved.getId());

        // then
        assertThat(actualQuestion).isEmpty();

    }
}