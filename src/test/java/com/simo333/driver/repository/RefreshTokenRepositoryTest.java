package com.simo333.driver.repository;

import com.simo333.driver.model.RefreshToken;
import com.simo333.driver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFind_whenCorrectName() {
        // given
        Instant expiryDate = Instant.now().plus(1, ChronoUnit.DAYS);
        String expectedName = "token";
        String otherName = "other";
        RefreshToken expectedToken = new RefreshToken();
        expectedToken.setToken(expectedName);
        expectedToken.setExpiryDate(expiryDate);
        RefreshToken otherToken = new RefreshToken();
        otherToken.setToken(otherName);
        otherToken.setExpiryDate(expiryDate);

        // when
        tokenRepository.save(expectedToken);
        tokenRepository.save(otherToken);
        Optional<RefreshToken> actualToken = tokenRepository.findByToken(expectedName);

        // then
        assertThat(actualToken).isEqualTo(Optional.of(expectedToken));
    }


    @Transactional
    @Test
    void shouldNotExist_whenRemoved() {
        // given
        Instant expiryDate = Instant.now().plus(1, ChronoUnit.DAYS);
        String expectedName = "token";
        RefreshToken token = new RefreshToken();
        token.setToken(expectedName);
        token.setExpiryDate(expiryDate);
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        token.setUser(user);

        // when
        userRepository.save(user);
        tokenRepository.save(token);
        tokenRepository.deleteByUser(user);
        Optional<RefreshToken> actualResult = tokenRepository.findByToken(expectedName);

        // then
        assertThat(actualResult).isEmpty();
    }
}