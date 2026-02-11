package com.javaapp.backend_7irfati.Dtos.Artisan;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArtisanProjectResponse {
    private Long id;
    private String title;
    private String description;
    private List<String> photoUrls;
    private LocalDateTime createdAt;
}
