package com.javaapp.backend_7irfati.controller.auth;


import com.javaapp.backend_7irfati.Dtos.auth.*;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.RefreshToken;
import com.javaapp.backend_7irfati.repository.RefreshTokenRepository;
import com.javaapp.backend_7irfati.security.JwtTokenProvider;
import com.javaapp.backend_7irfati.service.AuthService;
import com.javaapp.backend_7irfati.service.impl.RefreshTokenService;
import com.javaapp.backend_7irfati.utilis.LookupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LookupService lookupService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        AuthResponse registerResponse = authService.registerUser(request);
        return ResponseEntity.ok(registerResponse);
    }
    @PostMapping("/login")

    public ResponseEntity<?>login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue("refreshToken") String refreshTokenValue
    ) {

        RefreshToken oldToken = refreshTokenService.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        RefreshToken newToken = refreshTokenService.rotateRefreshToken(oldToken);

        String newAccessToken =
                jwtTokenProvider.generateTokenFromUser(newToken.getUser());

        ResponseCookie newRefreshCookie = ResponseCookie.from("refreshToken", newToken.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh-token")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                .body(new AuthResponse(newAccessToken, null));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        RefreshToken token = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        token.setRevoked(true);
        refreshTokenRepository.save(token);

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/register_data")
    public ResponseEntity<?> getRegisterData() {
        return ResponseEntity.ok(lookupService.getRegisterData());
    }
}
