package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_generator")
    @SequenceGenerator(name = "question_generator", sequenceName = "question_seq")
    private Long id;
    @NotNull
    @Size(min = 3, max = 255)
    private String contents;
    @OneToMany(
            mappedBy = "question",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Answer> answers = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id")
    private Training training;
    //TODO files (photos or videos) maintenance
}
