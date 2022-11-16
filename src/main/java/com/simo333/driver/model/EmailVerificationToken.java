package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "email_verification_tokens")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationToken {
    private static final int EXPIRATION_TIME_HOURS = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private Instant expirationDate;

    @PrePersist
    private void prePersist() {
        expirationDate = Instant.now().plus(EXPIRATION_TIME_HOURS, ChronoUnit.HOURS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailVerificationToken that = (EmailVerificationToken) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
