package com.javaapp.backend_7irfati.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many verification requests belong to one Artisan
    @ManyToOne
    @JoinColumn(name = "artisan_id", nullable = false)
    private Artisan artisan;

    private String documentUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    private LocalDateTime reviewedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
