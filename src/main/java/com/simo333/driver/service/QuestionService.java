package com.simo333.driver.service;

import com.simo333.driver.model.Question;
import com.simo333.driver.payload.question.QuestionCreateRequest;
import com.simo333.driver.payload.question.QuestionUpdateRequest;

import java.util.List;

public interface QuestionService {

    Question findOne(Long questionId);

    List<Question> findAll();

    Question save(QuestionCreateRequest request);

    Question update(Long questionId, QuestionUpdateRequest request);

    void delete(Long questionId);
}
