package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "completed_quizzes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompletedQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_generator")
    @SequenceGenerator(name = "quiz_generator", sequenceName = "quiz_seq")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advice_id")
    private Advice advice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private User user;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "quiz_answers", joinColumns = @JoinColumn(name = "completed_quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id"))
    private List<Answer> givenAnswers;
    private int score;
    @Column(updatable = false)
    private Instant timeStamp;

    @PrePersist
    public void prePersist() {
        timeStamp = Instant.now();
    }
}
