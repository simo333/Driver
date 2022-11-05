package com.simo333.driver.payload.security;

import com.simo333.driver.annotations.email_validator.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(min = 6, max = 120)
    private String username;
    @ValidEmail
    private String email;
    @NotBlank
    @Size(min = 8, max = 120)
    private String password;
}
