package com.simo333.driver.service.impl;

import com.simo333.driver.model.EmailVerificationToken;
import com.simo333.driver.model.User;
import com.simo333.driver.repository.EmailVerificationTokenRepository;
import com.simo333.driver.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
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
    public EmailVerificationToken getVerificationToken(String token) {
        return repository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Email verification token does not exists. For token: " + token));
    }

    @Override
    public void deleteToken(String token) {
        repository.deleteByToken(token);
    }
}
