package com.simo333.driver.payload.advice;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AdviceQuestionsRequest {
    @NotNull
    private Set<Long> questions;
}
