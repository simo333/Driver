package com.simo333.driver.payload.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PatchUserRequest {
    @NotBlank
    @Size(min = 8, max = 120)
    private String password;
}
