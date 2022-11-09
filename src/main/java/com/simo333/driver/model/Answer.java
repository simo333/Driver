package com.simo333.driver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "answers")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_generator")
    @SequenceGenerator(name = "answer_generator", sequenceName = "answer_seq")
    private Long id;
    @NotNull
    @Size(min = 3, max = 100)
    private String contents;
    @NotNull
    private boolean isCorrect;
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @JsonIgnore
    private Question question;
}
