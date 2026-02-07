package com.javaapp.backend_7irfati.controller.auth;


import com.javaapp.backend_7irfati.Dtos.auth.RegisterRequest;
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
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authService.registerUser(request);
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("/login")

    public String login(){
        return "Login Page";
    }
}
