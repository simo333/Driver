package com.simo333.driver.service;

import com.simo333.driver.model.Quiz;

public interface QuizService {

    Quiz save(Quiz quiz);

    Quiz findOne(Long quizId);
}
