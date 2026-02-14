package com.javaapp.backend_7irfati.service.impl;

import com.javaapp.backend_7irfati.Dtos.auth.AuthResponse;
import com.javaapp.backend_7irfati.Dtos.auth.LoginRequest;
import com.javaapp.backend_7irfati.Dtos.auth.RegisterRequest;
import com.javaapp.backend_7irfati.Dtos.auth.RegisterResponse;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.*;
import com.javaapp.backend_7irfati.exception.EmailAlreadyExistsException;
import com.javaapp.backend_7irfati.repository.RoleRepository;
import com.javaapp.backend_7irfati.repository.UserRepository;
import com.javaapp.backend_7irfati.security.JwtTokenProvider;
import com.javaapp.backend_7irfati.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Setter
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse registerUser(RegisterRequest request) {

        // 1️ Vérifier si l’email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // 2️ Récupérer le rôle par défaut
        Role defaultRole = roleRepository.findByName(RoleName.CLIENT)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        // 3️ Créer l’utilisateur
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .language(Language.valueOf(request.getLanguage().toUpperCase()))
                .enabled(true)
                .roles(Set.of(defaultRole))
                .build();


        userRepository.save(user);


        String accessToken = jwtTokenProvider.generateTokenFromUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateToken(authentication);
            RefreshToken refreshToken = refreshTokenService.createOrReplace(user);

            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/api/auth/refresh-token")
                    .maxAge(Duration.ofDays(7))
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(new AuthResponse(accessToken, null));

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            // ✅ Return directly here — never reaches JwtAuthEntryPoint
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "Unauthorized",
                            "message", "Email ou mot de passe incorrect"
                    ));
        }
    }



    public AuthResponse refreshToken(String refreshToken) {

        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        User user = token.getUser();

        String newAccessToken = jwtTokenProvider.generateTokenFromUser(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // rotation plus tard
                .build();
    }



}
