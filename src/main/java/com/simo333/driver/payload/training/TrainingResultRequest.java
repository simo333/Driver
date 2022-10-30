package com.simo333.driver.payload.training;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class TrainingResultRequest {
    @NotNull
    private Long trainingId;
    private final Set<Long> answers = new HashSet<>();
}
