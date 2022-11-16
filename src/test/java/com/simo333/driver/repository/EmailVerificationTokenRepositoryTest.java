package com.simo333.driver.repository;

import com.simo333.driver.model.EmailVerificationToken;
import com.simo333.driver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class EmailVerificationTokenRepositoryTest {

    @Autowired
    private EmailVerificationTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldFind_whenCorrectName() {
        // given
        String expectedName = "token";
        String otherName = "token2";
        EmailVerificationToken expectedToken = new EmailVerificationToken();
        expectedToken.setToken(expectedName);
        EmailVerificationToken otherToken = new EmailVerificationToken();
        otherToken.setToken(otherName);
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        expectedToken.setUser(user);
        otherToken.setUser(user);

        // when
        userRepository.save(user);
        tokenRepository.save(expectedToken);
        tokenRepository.save(otherToken);
        Optional<EmailVerificationToken> actualToken = tokenRepository.findByToken(expectedName);

        // then
        assertThat(actualToken).isEqualTo(Optional.of(expectedToken));
    }

    @Test
    void shouldFind_whenCorrectUser() {
        // given
        String expectedName = "token";
        String otherName = "token2";
        EmailVerificationToken expectedToken = new EmailVerificationToken();
        expectedToken.setToken(expectedName);
        EmailVerificationToken otherToken = new EmailVerificationToken();
        otherToken.setToken(otherName);
        User expectedUser = new User();
        expectedUser.setUsername("username5");
        expectedUser.setPassword("password");
        expectedUser.setEmail("email5@email.com");
        User otherUser = new User();
        otherUser.setUsername("username6");
        otherUser.setPassword("password2");
        otherUser.setEmail("email62@email.com");
        expectedToken.setUser(expectedUser);
        otherToken.setUser(otherUser);

        // when
        userRepository.save(expectedUser);
        userRepository.save(otherUser);
        tokenRepository.save(expectedToken);
        tokenRepository.save(otherToken);
        Optional<EmailVerificationToken> actualToken = tokenRepository.findByUser(expectedUser);

        // then
        assertThat(actualToken).isEqualTo(Optional.of(expectedToken));
    }

    @Transactional
    @Test
    void deleteByToken() {
        // given
        String expectedName = "token";
        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken(expectedName);
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");
        token.setUser(user);

        // when
        userRepository.save(user);
        tokenRepository.save(token);
        tokenRepository.deleteByToken(expectedName);
        Optional<EmailVerificationToken> actualToken = tokenRepository.findByToken(expectedName);

        // then
        assertThat(actualToken).isEmpty();
    }
}