package com.simo333.driver.service.impl;

import com.simo333.driver.exception.UniqueNameViolationException;
import com.simo333.driver.model.Advice;
import com.simo333.driver.model.Tag;
import com.simo333.driver.payload.advice.AdviceCreateRequest;
import com.simo333.driver.payload.advice.AdviceUpdateRequest;
import com.simo333.driver.repository.AdviceRepository;
import com.simo333.driver.repository.TagRepository;
import com.simo333.driver.service.AdviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class AdviceServiceImplTest {

    @Autowired
    private AdviceService adviceService;
    @Autowired
    private AdviceRepository adviceRepository;
    @Autowired
    private TagRepository tagRepository;


    @Test
    void shouldFindAll() {
        // given
        Advice advice1 = new Advice();
        advice1.setTitle("advice1");
        advice1.setDescription("advice1");
        Advice advice2 = new Advice();
        advice2.setTitle("advice2");
        advice2.setDescription("advice2");

        // when
        adviceRepository.saveAll(List.of(advice1, advice2));
        Page<Advice> actualAdvices = adviceService.findAll(PageRequest.of(0, 20));

        // then
        assertThat(actualAdvices)
                .hasSize(2)
                .contains(advice1, advice2);

    }

    @Test
    void shouldFindAllByTag() {
        // given
        Tag tag = new Tag();
        tag.setName("tag1");
        Advice advice1 = new Advice();
        advice1.setTitle("advice3");
        advice1.setDescription("advice3");
        advice1.setTags(Set.of(tag));
        Advice advice2 = new Advice();
        advice2.setTitle("advice4");
        advice2.setDescription("advice4");

        // when
        tagRepository.save(tag);
        adviceRepository.saveAll(List.of(advice1, advice2));
        Page<Advice> actualAdvices = adviceService.findAllByTag(tag.getName(), PageRequest.of(0, 20));

        // then
        assertThat(actualAdvices)
                .hasSize(1)
                .contains(advice1);


    }

    @Test
    void shouldFindOne() {
        // given
        Advice expectedAdvice = new Advice();
        expectedAdvice.setTitle("advice5");
        expectedAdvice.setDescription("advice5");

        // when
        adviceRepository.save(expectedAdvice);
        Advice actualAdvice = adviceService.findOne(expectedAdvice.getId());

        // then
        assertThat(actualAdvice).isEqualTo(expectedAdvice);

    }

    @Test
    void shouldThrowException_whenNotFind() {
        // given
        Long badId = -1L;

        // then
        assertThatThrownBy(() -> adviceService.findOne(badId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Advice with id '%s' not found", badId));
    }

    @Test
    void shouldSave() {
        // given
        String expectedTitle = "title";
        String expectedDescription = "description";
        AdviceCreateRequest request = new AdviceCreateRequest();
        request.setTitle(expectedTitle);
        request.setDescription(expectedDescription);

        // when
        Advice actualAdvice = adviceService.save(request);

        // then
        assertThat(actualAdvice.getTitle()).isEqualTo(expectedTitle);
        assertThat(actualAdvice.getDescription()).isEqualTo(expectedDescription);

    }

    @Test
    void shouldThrowException_whenTitleNotUnique() {
        // given
        String title = "sameTitle";
        AdviceCreateRequest request = new AdviceCreateRequest();
        request.setTitle(title);
        request.setDescription("description");
        AdviceCreateRequest sameRequest = new AdviceCreateRequest();
        sameRequest.setTitle(title);
        sameRequest.setDescription("description");

        // when
        adviceService.save(request);

        // then
        assertThatThrownBy(() -> adviceService.save(sameRequest))
                .isInstanceOf(UniqueNameViolationException.class)
                .hasMessageContaining("title already exists");

    }

    @Test
    @Transactional
    void shouldUpdate() {
        // given
        String oldTitle = "oldTitle";
        String oldDescription = "oldDescription";
        String newTitle = "newTitle";
        String newDescription = "newDescription";

        Tag newTag = new Tag();
        newTag.setName("newTag");
        Advice advice = new Advice();
        advice.setTitle(oldTitle);
        advice.setDescription(oldDescription);

        AdviceUpdateRequest request = new AdviceUpdateRequest();
        request.setDescription(newDescription);
        request.setTitle(newTitle);
        request.setTagNames(Set.of(newTag.getName()));

        // when
        tagRepository.save(newTag);
        Advice oldAdvice = adviceRepository.save(advice);
        Advice updatedAdvice = adviceService.update(oldAdvice.getId(), request);

        // then
        assertThat(updatedAdvice.getTitle()).isEqualTo(newTitle);
        assertThat(updatedAdvice.getDescription()).isEqualTo(newDescription);
        assertThat(updatedAdvice.getTags()).isEqualTo(Set.of(newTag));

    }

    @Test
    void shouldDelete() {
        // given
        Advice advice = new Advice();
        advice.setTitle("toDelete");
        advice.setDescription("description");

        // when
        Advice saved = adviceRepository.save(advice);
        adviceService.delete(saved.getId());
        Optional<Advice> actualAdvice = adviceRepository.findById(saved.getId());

        // then
        assertThat(actualAdvice).isEmpty();

    }

}