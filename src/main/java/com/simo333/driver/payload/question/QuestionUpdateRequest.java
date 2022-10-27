package com.simo333.driver.payload.question;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class QuestionUpdateRequest {
    @Size(min = 3, max = 255)
    private String questionText;
    private Long trainingId;
}
