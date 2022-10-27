package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainings")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_generator")
    @SequenceGenerator(name = "training_generator", sequenceName = "training_seq")
    private Long id;
    @OneToOne
    private Advice advice;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "training")
    private Set<Question> questions = new HashSet<>();
}
