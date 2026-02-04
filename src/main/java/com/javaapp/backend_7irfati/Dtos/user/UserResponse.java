package com.javaapp.backend_7irfati.Dtos.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String language;
}
