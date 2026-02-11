package com.javaapp.backend_7irfati.controller.auth;


import com.javaapp.backend_7irfati.Dtos.auth.*;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.RefreshToken;
import com.javaapp.backend_7irfati.repository.RefreshTokenRepository;
import com.javaapp.backend_7irfati.security.JwtTokenProvider;
import com.javaapp.backend_7irfati.service.AuthService;
import com.javaapp.backend_7irfati.service.impl.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        AuthResponse registerResponse = authService.registerUser(request);
        return ResponseEntity.ok(registerResponse);
    }
    @PostMapping("/login")

    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshToken oldToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        RefreshToken newToken = refreshTokenService.rotateRefreshToken(oldToken);

        String newAccessToken = jwtTokenProvider.generateTokenFromUser(newToken.getUser());

        return ResponseEntity.ok(new AuthResponse(newAccessToken, newToken.getToken()));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        RefreshToken token = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        token.setRevoked(true);
        refreshTokenRepository.save(token);

        return ResponseEntity.ok("Logged out successfully");
    }
}
