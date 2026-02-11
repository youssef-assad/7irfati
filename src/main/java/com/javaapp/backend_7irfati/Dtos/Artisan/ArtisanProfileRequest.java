package com.javaapp.backend_7irfati.Dtos.Artisan;

import lombok.Data;

import java.util.List;

@Data
public class ArtisanProfileRequest {
    private String description;
    private int yearsExperience;
    private Long cityId;
    private List<Long> categoryIds;
    private List<String> profilePhotoUrls;
}