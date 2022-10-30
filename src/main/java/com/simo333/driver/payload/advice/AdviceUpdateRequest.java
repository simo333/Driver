package com.simo333.driver.payload.advice;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class AdviceUpdateRequest {
    @Size(min = 3, max = 100)
    private String title;
    private String description;
    private Set<String> tagNames;
    @NotNull(message = "Cannot be null. Choose 'REPLACE' to replace current tags or 'REMOVE_ALL' to remove all tags from advice.")
    private Option tagsChange;

    public enum Option {REPLACE, REMOVE_ALL}
}
