package com.simo333.driver.service;

import com.simo333.driver.model.Question;
import com.simo333.driver.payload.question.QuestionRequest;

import java.util.List;

public interface QuestionService {

    Question findOne(Long questionId);

    List<Question> findAll();

    Question save(QuestionRequest request);

    Question update(Question question);

    void delete(Long questionId);
}
