package com.simo333.driver.payload.question;

import com.simo333.driver.payload.answer.AnswerRequest;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionRequest {
    @NotNull
    @Size(min = 3, max = 255)
    private String questionText;
    @NotNull
    @Size(min = 2, max = 4)
    private List<@Valid AnswerRequest> answers;
}
