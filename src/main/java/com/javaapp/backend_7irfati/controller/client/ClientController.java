package com.javaapp.backend_7irfati.controller.client;

import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.User;
import com.javaapp.backend_7irfati.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client")
public class ClientController {
   //private final UserService userService;
    @GetMapping
    public String hello(){
        return "Hello Client";
    }
//    @GetMapping("/me")
//    private UserResponse getMyProfile(
//            @AuthenticationPrincipal User user
//            ){
//        return userService.getMyProfile(user);
//    }
}
