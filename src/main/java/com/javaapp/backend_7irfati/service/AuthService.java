package com.javaapp.backend_7irfati.service;

import com.javaapp.backend_7irfati.Dtos.auth.RegisterRequest;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;

public interface AuthService {
    UserResponse registerUser(RegisterRequest request);
}
