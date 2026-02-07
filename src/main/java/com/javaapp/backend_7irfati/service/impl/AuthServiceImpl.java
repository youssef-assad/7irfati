package com.javaapp.backend_7irfati.service.impl;

import com.javaapp.backend_7irfati.Dtos.auth.RegisterRequest;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.Language;
import com.javaapp.backend_7irfati.entity.Role;
import com.javaapp.backend_7irfati.entity.RoleName;
import com.javaapp.backend_7irfati.entity.User;
import com.javaapp.backend_7irfati.repository.RoleRepository;
import com.javaapp.backend_7irfati.repository.UserRepository;
import com.javaapp.backend_7irfati.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;

@Setter
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role defaultRole = roleRepository.findByName(RoleName.CLIENT)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .language(Language.valueOf(request.getLanguage().toUpperCase())) // convert String to Enum
                .enabled(true)
                .roles(Collections.singleton(defaultRole))
                .build();

        userRepository.save(user);

        return UserResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(request.getPhone())
                .language(request.getLanguage())
                .id(user.getId())
                .build();
    }

}
