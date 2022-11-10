package com.simo333.driver.payload.advice;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class AdviceUpdateRequest {
    @Size(min = 3, max = 100)
    private String title;
    private String description;
    private Set<String> tagNames;
}
