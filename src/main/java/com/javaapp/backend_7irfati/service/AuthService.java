package com.javaapp.backend_7irfati.service;

import com.javaapp.backend_7irfati.Dtos.auth.AuthResponse;
import com.javaapp.backend_7irfati.Dtos.auth.LoginRequest;
import com.javaapp.backend_7irfati.Dtos.auth.RegisterRequest;
import com.javaapp.backend_7irfati.Dtos.auth.RegisterResponse;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;

public interface AuthService {
    AuthResponse registerUser(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
}
