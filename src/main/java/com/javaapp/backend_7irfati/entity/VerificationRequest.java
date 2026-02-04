package com.javaapp.backend_7irfati.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_requests")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class VerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Artisan artisan;

    private String documentUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    private LocalDateTime reviewedAt;
}
