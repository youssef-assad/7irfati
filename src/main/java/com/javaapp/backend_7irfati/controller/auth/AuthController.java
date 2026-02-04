package com.javaapp.backend_7irfati.controller.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @GetMapping("/register")
    public String register(){
        return  "Hello Register :";
    }
    @GetMapping("/login")

    public String login(){
        return "Login Page";
    }
}
