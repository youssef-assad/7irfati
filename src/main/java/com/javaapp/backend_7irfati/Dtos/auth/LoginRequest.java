package com.javaapp.backend_7irfati.Dtos.auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
