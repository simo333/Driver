package com.simo333.driver.service;


import com.simo333.driver.model.Answer;
import com.simo333.driver.model.Question;
import com.simo333.driver.payload.answer.AnswerRequest;

import java.util.List;

public interface AnswerService {

    Answer findById(Long answerId);

    List<Answer> findAllByQuestionId(Long questionId);

    Answer save(Answer tag);

    Answer update(Answer tag);

    void delete(Long tagId);

    void saveForQuestion(Question question, List<AnswerRequest> answers);
}
