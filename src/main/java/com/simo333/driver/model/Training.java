package com.simo333.driver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainings")
@Getter
@Setter
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_generator")
    @SequenceGenerator(name = "training_generator", sequenceName = "training_seq")
    private Long id;
    @OneToOne
    private Advice advice;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Question> questions = new HashSet<>();
}
