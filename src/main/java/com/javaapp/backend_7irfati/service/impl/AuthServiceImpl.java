package com.javaapp.backend_7irfati.service.impl;

import com.javaapp.backend_7irfati.Dtos.auth.LoginRequest;
import com.javaapp.backend_7irfati.Dtos.auth.RegisterRequest;
import com.javaapp.backend_7irfati.Dtos.auth.RegisterResponse;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.Language;
import com.javaapp.backend_7irfati.entity.Role;
import com.javaapp.backend_7irfati.entity.RoleName;
import com.javaapp.backend_7irfati.entity.User;
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
    @Override
    public RegisterResponse registerUser(RegisterRequest request) {

        // 1️⃣ Vérifier si l’email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // 2️⃣ Récupérer le rôle par défaut
        Role defaultRole = roleRepository.findByName(RoleName.CLIENT)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        // 3️⃣ Créer l’utilisateur
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

        // 4️⃣ Sauvegarder en DB
        userRepository.save(user);

        // 5️⃣ Générer le JWT (LOGIN DIRECT 🔥)
        String jwt = jwtTokenProvider.generateTokenFromUser(user);

        // 6️⃣ Retourner la réponse
        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .language(user.getLanguage().name())
                .jwt(jwt)
                .build();
    }

    @Override
    public RegisterResponse login(LoginRequest request) {
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        assert user != null;
        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .language(user.getLanguage().name())
                .jwt(jwtTokenProvider.generateToken(authentication))
                .build();

    }


}
