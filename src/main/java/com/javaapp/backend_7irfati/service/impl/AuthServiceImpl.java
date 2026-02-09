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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
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

        // 4 Sauvegarder en DB
        userRepository.save(user);

        // 5️ Générer le JWT (LOGIN DIRECT 🔥)
        String accessToken = jwtTokenProvider.generateTokenFromUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        assert user != null;
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();

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
