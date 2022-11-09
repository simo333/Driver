package com.simo333.driver.payload.training;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingResultResponse {
    private long score;
    private long totalPoints;
    private boolean passed;
}
