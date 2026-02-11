package com.javaapp.backend_7irfati.service.impl;

import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProfileResponse;
import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProjectResponse;
import com.javaapp.backend_7irfati.entity.*;
import com.javaapp.backend_7irfati.service.ArtisanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArtisanServiceImpl implements ArtisanService {
    public ArtisanProfileResponse mapToDto(Artisan artisan) {
        String lang = artisan.getUser().getLanguage().name();
        ArtisanProfileResponse dto = new ArtisanProfileResponse();
        dto.setId(artisan.getId());
        dto.setDescription(artisan.getDescription());
        dto.setYearsExperience(artisan.getYearsExperience());
        dto.setVerified(artisan.isVerified());
        dto.setRatingAvg(artisan.getRatingAvg());
        dto.setCityName(lang.equals("AR")
                ? artisan.getCity().getNameAr()
                : artisan.getCity().getNameFr());
        dto.setCategories(
                artisan.getCategories().stream()
                        .map(cat -> lang.equals("AR") ? cat.getNameAr() : cat.getNameFr()) // or getNameAr() depending on language
                        .collect(Collectors.toSet())
        );

        dto.setProfilePhotos(
                artisan.getPhotos().stream()
                        .map(ArtisanPhoto::getImageUrl)
                        .collect(Collectors.toList())
        );
        dto.setCreatedAt(artisan.getCreatedAt());
        return dto;
    }
    public ArtisanProjectResponse mapProjectToDto(ArtisanProject project) {
        ArtisanProjectResponse dto = new ArtisanProjectResponse();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setPhotoUrls(
                project.getPhotos().stream()
                        .map(ProjectPhoto::getUrl)
                        .collect(Collectors.toList())
        );
        dto.setCreatedAt(project.getCreatedAt());
        return dto;
    }


}
