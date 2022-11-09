package com.simo333.driver.service;


import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.answer.AnswerCreateRequest;
import com.simo333.driver.payload.answer.AnswerUpdateRequest;

import java.util.List;

public interface AnswerService {

    Answer findById(Long answerId);

    List<Answer> findAllByQuestionId(Long questionId);

    void saveForQuestion(Question question, List<AnswerCreateRequest> answers);

    Answer update(Long id, AnswerUpdateRequest request);

    void delete(Long answerId);

}
