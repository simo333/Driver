package com.simo333.driver.repository;

import com.simo333.driver.model.Advice;
import com.simo333.driver.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class AdviceRepositoryTest {

    @Autowired
    private AdviceRepository adviceRepository;
    @Autowired
    private TagRepository tagRepository;


    @Test
    void shouldFindByTag() {
        // given
        String tagName = "tag";
        Tag tag = new Tag();
        tag.setName(tagName);

        String title = "title";
        Advice correctAdvice = new Advice();
        correctAdvice.setTitle(title);
        correctAdvice.setDescription("description");
        correctAdvice.setTags(Set.of(tag));
        Advice incorrectAdvice = new Advice();
        incorrectAdvice.setTitle("otherTitle");
        incorrectAdvice.setDescription("description");

        // when
        tagRepository.save(tag);
        adviceRepository.save(correctAdvice);
        adviceRepository.save(incorrectAdvice);
        List<Advice> actualAdvices = adviceRepository.findAllByTagsName(tagName, PageRequest.of(0, 20)).toList();

        // then
        assertThat(actualAdvices)
                .hasSize(1)
                .contains(correctAdvice);
    }

    @Test
    void whenExists_thenFindAll() {
        // given
        String expectedTitle = "title";
        Advice advice = new Advice();
        advice.setTitle(expectedTitle);
        advice.setDescription("description");
        adviceRepository.save(advice);

        // when
        boolean exists = adviceRepository.existsByTitle(expectedTitle);

        // then
        assertTrue(exists);
    }

    @Test
    void whenNotExists_thenNotFind() {
        // given
        String badTitle = "title";

        // when
        boolean exists = adviceRepository.existsByTitle(badTitle);

        // then
        assertFalse(exists);
    }
}