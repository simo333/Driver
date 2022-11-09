package com.simo333.driver.service.impl;

import com.simo333.driver.exception.EmailTokenException;
import com.simo333.driver.model.EmailVerificationToken;
import com.simo333.driver.model.User;
import com.simo333.driver.repository.EmailVerificationTokenRepository;
import com.simo333.driver.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository repository;

    @Override
    public EmailVerificationToken createVerificationToken(User user) {
        repository.findByUser(user).ifPresent(repository::delete);

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .user(user)
                .token(token)
                .build();
        log.info("Created a new EmailVerificationToken: {}", token);
        return repository.save(verificationToken);
    }

    @Override
    public void confirmRegistration(String token) {
        EmailVerificationToken verificationToken = verifyToken(token);
        User user = verificationToken.getUser();
        user.setEnabled(true);
    }

    @Override
    public EmailVerificationToken verifyToken(String token) {
        EmailVerificationToken verificationToken = getVerificationToken(token);
        if (Instant.now().isAfter(verificationToken.getExpirationDate())) {
            deleteToken(token);
            throw new EmailTokenException("Email Verification Token expired. For token: " + token);
        }
        deleteToken(token);
        return verificationToken;
    }

    @Transactional(readOnly = true)
    @Override
    public EmailVerificationToken getVerificationToken(String token) {
        return repository.findByToken(token).orElseThrow(
                () -> new EmailTokenException("Email Verification Token does not exists. For token: " + token));
    }


    @Override
    public void deleteToken(String token) {
        repository.deleteByToken(token);
    }
}
