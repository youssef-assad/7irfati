package com.javaapp.backend_7irfati.service;

import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.User;

public interface UserService {
    UserResponse getMyProfile(User user);
}
