package com.javaapp.backend_7irfati.controller.auth;


import com.javaapp.backend_7irfati.Dtos.auth.*;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        AuthResponse registerResponse = authService.registerUser(request);
        return ResponseEntity.ok(registerResponse);
    }
    @PostMapping("/login")

    public AuthResponse login(
            @RequestBody LoginRequest request
            ){
        return authService.login(request);
    }
    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        return authService.refreshToken(request.getRefreshToken());
    }

}
