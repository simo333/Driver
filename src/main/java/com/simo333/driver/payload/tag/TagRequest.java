package com.simo333.driver.payload.tag;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TagRequest {
    @NotNull
    @Size(min = 3, max = 30)
    private String tagName;
}
