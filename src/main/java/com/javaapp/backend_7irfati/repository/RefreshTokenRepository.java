package com.javaapp.backend_7irfati.repository;

import com.javaapp.backend_7irfati.entity.RefreshToken;
import com.javaapp.backend_7irfati.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);

}
