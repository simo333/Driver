package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "advices")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advice_generator")
    @SequenceGenerator(name = "advice_generator", sequenceName = "advice_seq")
    private Long id;
    @NotNull
    @Size(min = 3, max = 100)
    private String title;
    @NotBlank
    private String description;
    @ManyToMany
    @JoinTable(name = "advice_tags",
            joinColumns = @JoinColumn(name = "advice_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", updatable = false, referencedColumnName = "id")
    private Training training;
    @OneToMany(mappedBy = "advice", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();
    @Positive
    private int shareQuantity = 0;
    @Column(updatable = false)
    private LocalDate date;

    @PrePersist
    public void prePersist() {
        date = LocalDate.now();
    }
}
