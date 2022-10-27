package com.simo333.driver.service;

import com.simo333.driver.model.Advice;
import com.simo333.driver.payload.advice.AdviceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdviceService {

    Page<Advice> findAll(Pageable page);

    Page<Advice> findAllByTag(String tagName, Pageable page);

    Advice findOne(Long adviceId);

    Advice save(AdviceRequest request);

    Advice update(Long adviceId, AdviceRequest request);

    void delete(Long adviceId);
}
