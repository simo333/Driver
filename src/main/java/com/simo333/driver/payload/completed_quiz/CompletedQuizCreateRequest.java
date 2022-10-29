package com.simo333.driver.payload.completed_quiz;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompletedQuizCreateRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long adviceId;
    private List<Long> answers;
}
