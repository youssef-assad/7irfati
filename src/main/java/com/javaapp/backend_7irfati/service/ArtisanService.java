package com.javaapp.backend_7irfati.service;

import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProfileRequest;
import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArtisanService {
    ArtisanProfileResponse createArtisanProfile(Long userId,ArtisanProfileRequest request,  List<MultipartFile> profilePhotos);
}
