package com.simo333.driver.repository;

import com.simo333.driver.model.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AdviceRepositoryTest {

    @Autowired
    private AdviceRepository adviceRepository;


    @Test
    void findAllByTagsName() {

    }

    @Test
    void existsByTitle() {
        //given
        Advice advice = new Advice();
        advice.setTitle("title");
        advice.setDescription("description");
        adviceRepository.save(advice);

        //when
        boolean exists = adviceRepository.existsByTitle("title");

        //then
        assertTrue(exists);

    }
}