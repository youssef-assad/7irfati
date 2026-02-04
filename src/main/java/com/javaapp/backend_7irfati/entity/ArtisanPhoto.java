package com.javaapp.backend_7irfati.entity;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "artisan_photos")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class ArtisanPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan_id")
    private Artisan artisan;

    @CreationTimestamp
    private LocalDateTime uploadedAt;
}
