package com.simo333.driver.payload.quiz;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class QuizScoreRequest {
    private Long quizId;
    private Set<Long> answers = new HashSet<>();
}
