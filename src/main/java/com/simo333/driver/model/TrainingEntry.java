package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "trainings")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Question> questions;
    private Long userId;
}
