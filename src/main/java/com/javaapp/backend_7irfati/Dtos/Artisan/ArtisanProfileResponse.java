package com.javaapp.backend_7irfati.Dtos.Artisan;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ArtisanProfileResponse {
    private Long id;
    private String description;
    private int yearsExperience;
    private boolean verified;
    private Double ratingAvg;
    private String cityName;
    private Set<String> categories;
    private List<String> profilePhotos;
    private LocalDateTime createdAt;
}
