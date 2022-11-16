package com.simo333.driver.repository;

import com.simo333.driver.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void shouldFindExpectedTag() {
        // given
        String tagName = "tagName";
        Tag expectedTag = new Tag();
        expectedTag.setName(tagName);
        Tag notExpectedTag = new Tag();
        notExpectedTag.setName("notExpectedTag");

        // when
        tagRepository.save(expectedTag);
        tagRepository.save(notExpectedTag);
        Optional<Tag> actualTag = tagRepository.findByName(tagName);

        // then
        assertThat(actualTag).isEqualTo(Optional.of(expectedTag));
    }

    @Test
    void shouldReturnTrue_whenExists() {
        // given
        String tagName = "someTag";
        Tag someTag = new Tag();
        someTag.setName(tagName);

        // when
        tagRepository.save(someTag);
        boolean exists = tagRepository.existsByName(tagName);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalse_whenNotExists() {
        // given
        String tagName = "tag";
        String badName = "bad";
        Tag tag = new Tag();
        tag.setName(tagName);

        // when
        tagRepository.save(tag);
        boolean exists = tagRepository.existsByName(badName);

        // then
        assertThat(exists).isFalse();
    }

}