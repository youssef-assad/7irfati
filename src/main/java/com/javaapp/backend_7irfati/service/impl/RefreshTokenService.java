package com.javaapp.backend_7irfati.service.impl;

import com.javaapp.backend_7irfati.entity.RefreshToken;
import com.javaapp.backend_7irfati.entity.User;
import com.javaapp.backend_7irfati.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken createRefreshToken(User user){
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        return refreshTokenRepository.save(refreshToken);

    }
    @Transactional
    public RefreshToken createOrReplace(User user) {
        refreshTokenRepository.deleteByUser(user);
        return createRefreshToken(user);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("REFRESH TOKEN EXPIRED");
        }
        return token;
    }
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        // 1️⃣ Vérifier expiration et revoked
        if (oldToken.isRevoked() || oldToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token invalid");
        }

        // 2️⃣ Révoquer l’ancien token
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        // 3️⃣ Créer un nouveau token pour le même user
        RefreshToken newToken = RefreshToken.builder()
                .user(oldToken.getUser())
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(newToken);
    }

    public void deleteByUser(User user) {
    }
}
