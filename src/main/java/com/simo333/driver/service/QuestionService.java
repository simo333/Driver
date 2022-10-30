package com.simo333.driver.service;

import com.simo333.driver.model.Question;
import com.simo333.driver.payload.question.QuestionCreateRequest;
import com.simo333.driver.payload.question.QuestionUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {

    Question findOne(Long questionId);

    Page<Question> findAll(Pageable page);

    Question save(QuestionCreateRequest request);

    Question update(Long questionId, QuestionUpdateRequest request);

    void delete(Long questionId);
}
