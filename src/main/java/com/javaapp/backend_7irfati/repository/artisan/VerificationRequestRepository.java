package com.javaapp.backend_7irfati.repository.artisan;

import com.javaapp.backend_7irfati.entity.VerificationRequest;
import com.javaapp.backend_7irfati.entity.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long> {

    List<VerificationRequest> findByArtisanId(Long artisanId);

    boolean existsByArtisanIdAndStatus(Long artisanId, VerificationStatus status);
}
