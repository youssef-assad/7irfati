package com.javaapp.backend_7irfati.Dtos.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private Long id;
    private String email;
    private String firstname;
    private String lastName;
    private String jwt;
    private String phone;
    private String language;

}
