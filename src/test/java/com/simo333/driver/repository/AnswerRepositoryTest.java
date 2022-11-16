package com.simo333.driver.repository;

import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void shouldFindAllAnswers_byGivenQuestionId() {
        // given
        Question question = new Question();
        question.setContents("Contents");
        Answer answer1 = new Answer();
        answer1.setCorrect(true);
        answer1.setContents("answer1");
        answer1.setQuestion(question);
        Answer answer2 = new Answer();
        answer2.setCorrect(false);
        answer2.setContents("answer1");
        answer2.setQuestion(question);

        // when
        questionRepository.save(question);
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        List<Answer> actualAnswers = answerRepository.findByQuestionId(question.getId());

        // then
        assertThat(actualAnswers)
                .hasSize(2)
                .contains(answer1, answer2);
    }

    @Test
    void countByQuestionId() {
        // given
        Question question = new Question();
        question.setContents("Contents");
        Answer answer1 = new Answer();
        answer1.setCorrect(true);
        answer1.setContents("answer1");
        answer1.setQuestion(question);
        Answer answer2 = new Answer();
        answer2.setCorrect(false);
        answer2.setContents("answer1");
        answer2.setQuestion(question);

        // when
        questionRepository.save(question);
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        int expectedCount = answerRepository.countByQuestionId(question.getId());

        // then
        assertThat(expectedCount).isEqualTo(2);
    }
}