package com.javaapp.backend_7irfati.repository.artisan;

import com.javaapp.backend_7irfati.entity.Artisan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtisanRepository extends JpaRepository<Artisan,Long> {
    Optional<Artisan> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
