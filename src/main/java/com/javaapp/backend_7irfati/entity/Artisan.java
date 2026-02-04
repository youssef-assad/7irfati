package com.javaapp.backend_7irfati.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "artisans")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Artisan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int yearsExperience;

    private boolean verified = false;

    private Double ratingAvg = 0.0;

    @ManyToOne
    private City city;

    @OneToMany(mappedBy = "artisan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtisanPhoto> photos;

    @ManyToMany
    @JoinTable(
            name = "artisan_categories",
            joinColumns = @JoinColumn(name = "artisan_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
