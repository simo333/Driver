package com.simo333.driver.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
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
    @Size(min = 3, max = 100)
    @Column(unique = true, nullable = false)
    private String title;
    @NotBlank
    private String description;
    @ManyToMany
    @JoinTable(name = "advice_tags",
            joinColumns = @JoinColumn(name = "advice_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();
    @OneToMany(mappedBy = "advice", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Question> questions = new HashSet<>();
    private int likesQuantity = 0;
    private int shareQuantity = 0;
    @Column(updatable = false)
    private Instant timeStamp;

    @PrePersist
    public void prePersist() {
        timeStamp = Instant.now();
    }
}
