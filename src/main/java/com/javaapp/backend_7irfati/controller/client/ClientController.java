package com.javaapp.backend_7irfati.controller.client;

import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProfileRequest;
import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProfileResponse;
import com.javaapp.backend_7irfati.Dtos.user.UserResponse;
import com.javaapp.backend_7irfati.entity.User;
import com.javaapp.backend_7irfati.service.ArtisanService;
import com.javaapp.backend_7irfati.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client")
public class ClientController {
   private final UserService userService;
   private final ArtisanService artisanService;
    @GetMapping
    public String hello(){
        return "Hello Client";
    }
    @GetMapping("/me")
    private UserResponse getMyProfile(
            @AuthenticationPrincipal User user
            ){
        return userService.getMyProfile(user);
    }
    @PostMapping(value = "/create_profile_artisan" , consumes = "multipart/form-data")
    public ResponseEntity<ArtisanProfileResponse> createArtisanProfile(
            @RequestPart("data") ArtisanProfileRequest request,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
            Authentication authentication
            ){
        User user = (User) authentication.getPrincipal();
        assert user != null;
        Long userId = user.getId();

        ArtisanProfileResponse response =
                artisanService.createArtisanProfile(userId, request, photos);
        return ResponseEntity.ok(response);

    }
}
