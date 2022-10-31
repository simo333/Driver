package com.simo333.driver.service;

import com.simo333.driver.model.RefreshToken;
import com.simo333.driver.model.User;

public interface RefreshTokenService {
    RefreshToken create(String userEmail);

    RefreshToken findByToken(String token);

    boolean verifyExpiration(RefreshToken token);

    void deleteByUser(User user);
}
