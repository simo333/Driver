package com.simo333.driver.service;

import com.simo333.driver.model.Advice;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.advice.AdviceCreateRequest;
import com.simo333.driver.payload.advice.AdviceQuestionsRequest;
import com.simo333.driver.payload.advice.AdviceUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface AdviceService {

    Page<Advice> findAll(Pageable page);

    Page<Advice> findAllByTag(String tagName, Pageable page);

    Advice findOne(Long adviceId);

    Advice save(AdviceCreateRequest request);

    Advice update(Long adviceId, AdviceUpdateRequest request);

    void addQuestionsToAdvice(Long adviceId, AdviceQuestionsRequest request);
    void removeQuestionsFromAdvice(Long adviceId, AdviceQuestionsRequest request);

    void delete(Long adviceId);
}
