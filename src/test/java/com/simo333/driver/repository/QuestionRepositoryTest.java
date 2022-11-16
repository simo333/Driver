package com.simo333.driver.repository;

import com.simo333.driver.model.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void shouldFindRandomQuestions_withGivenLimit() {
        // given
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Question question = new Question();
            question.setContents("question" + i);
            questions.add(question);
        }
        // when
        questionRepository.saveAll(questions);
        Set<Question> actualRandom = questionRepository.findRandom(5);

        // then
        assertThat(actualRandom).hasSize(5).containsAnyElementsOf(questions);
    }
}