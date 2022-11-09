package com.simo333.driver.payload.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {
    private String username;
    private Set<String> roles;
}

