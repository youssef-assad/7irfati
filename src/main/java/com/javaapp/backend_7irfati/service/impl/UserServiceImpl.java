package com.javaapp.backend_7irfati.service.impl;

import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.User;
import com.javaapp.backend_7irfati.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserResponse getMyProfile(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}
