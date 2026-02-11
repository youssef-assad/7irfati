package com.javaapp.backend_7irfati.Dtos.Artisan;

import lombok.Data;
import java.util.List;

@Data
public class ArtisanProjectRequest {
    private String title;
    private String description;
    private List<String> photoUrls;
}
