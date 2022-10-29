package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
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
    @OneToOne(fetch = FetchType.LAZY)
    private Advice advice;

}
