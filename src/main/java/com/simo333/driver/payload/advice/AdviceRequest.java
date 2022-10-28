package com.simo333.driver.payload.advice;

import com.simo333.driver.model.Tag;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class AdviceRequest {
    @Size(min = 3, max = 100)
    private String title;
    private String description;
    //TODO tag dto
    private Set<Tag> tags;
}
