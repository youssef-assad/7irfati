package com.javaapp.backend_7irfati.service.impl;

import com.javaapp.backend_7irfati.entity.RefreshToken;
import com.javaapp.backend_7irfati.entity.User;
import com.javaapp.backend_7irfati.repository.RefreshTokenRepository;
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

}
