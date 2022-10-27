package com.simo333.driver.payload.question;

import com.simo333.driver.payload.answer.AnswerCreateRequest;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionCreateRequest {
    @NotNull
    @Size(min = 3, max = 255)
    private String questionText;
    @NotNull
    @Size(min = 2, max = 4)
    private List<@Valid AnswerCreateRequest> answers;
}
