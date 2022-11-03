package com.simo333.driver.payload.user;

import com.simo333.driver.model.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserUpdateRequest {
    @Size(min = 6, max = 120)
    private String username;
    private Boolean enabled;
    private Set<Role> roles = new HashSet<>();
    @PositiveOrZero
    private Integer points;
}
