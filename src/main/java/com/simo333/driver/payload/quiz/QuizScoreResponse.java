package com.simo333.driver.payload.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuizScoreResponse {
    private long score;
    private int totalPoints;
    private long duration;
}
