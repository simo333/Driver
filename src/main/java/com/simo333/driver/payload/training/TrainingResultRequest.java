package com.simo333.driver.payload.training;

import com.simo333.driver.model.Answer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class TrainingResultRequest {
    @NotNull
    private Long trainingId;
    private final Set<Answer> givenAnswers = new HashSet<>();
}
