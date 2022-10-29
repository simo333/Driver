package com.simo333.driver.payload.completed_quiz;

import lombok.Data;

import java.time.Instant;

@Data
public class CompletedQuizResponse {
    private Instant timeStamp;
    private Long userId;
    private Long adviceId;
    private int score;
    private int pointsToScore;
}
