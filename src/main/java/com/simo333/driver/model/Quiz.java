package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "quizzes")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_generator")
    @SequenceGenerator(name = "quiz_generator", sequenceName = "quiz_seq")
    private Long id;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Question> questions;
    @OneToOne
    private User user;
    private Instant beginTimestamp;
    private Instant finishTimestamp;
    @PositiveOrZero
    private int score;

    @PrePersist
    private void prePersist() {
        beginTimestamp = Instant.now();
    }
}
