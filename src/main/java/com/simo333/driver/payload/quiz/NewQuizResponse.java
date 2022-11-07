package com.simo333.driver.payload.quiz;

import com.simo333.driver.model.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NewQuizResponse {
    private Long quizId;
    private Set<Question> questions;
    private int numberOfQuestions;
}
