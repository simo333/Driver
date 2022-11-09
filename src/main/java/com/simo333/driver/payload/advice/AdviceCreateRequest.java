package com.simo333.driver.payload.advice;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class AdviceCreateRequest {
    @NotNull
    @Size(min = 3, max = 100)
    private String title;
    @NotBlank
    private String description;
    private Set<String> tagNames;
}
