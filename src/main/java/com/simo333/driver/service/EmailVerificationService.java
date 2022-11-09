package com.simo333.driver.service;

import com.simo333.driver.model.EmailVerificationToken;
import com.simo333.driver.model.User;

public interface EmailVerificationService {

    EmailVerificationToken createVerificationToken(User user);

    EmailVerificationToken getVerificationToken(String token);

    EmailVerificationToken verifyToken(String token);

    void deleteToken(String token);

    void confirmRegistration(String token);
}
