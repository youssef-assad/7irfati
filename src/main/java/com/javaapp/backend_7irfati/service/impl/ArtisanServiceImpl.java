package com.javaapp.backend_7irfati.service.impl;

import com.cloudinary.utils.ObjectUtils;
import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProfileRequest;
import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProfileResponse;
import com.javaapp.backend_7irfati.Dtos.Artisan.ArtisanProjectResponse;
import com.javaapp.backend_7irfati.entity.*;
import com.javaapp.backend_7irfati.repository.UserRepository;
import com.javaapp.backend_7irfati.repository.artisan.ArtisanPhotoRepository;
import com.javaapp.backend_7irfati.repository.artisan.ArtisanRepository;
import com.javaapp.backend_7irfati.repository.optional.CategoryRepository;
import com.javaapp.backend_7irfati.repository.optional.CityRepository;
import com.javaapp.backend_7irfati.service.ArtisanService;
import com.javaapp.backend_7irfati.utilis.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import com.cloudinary.*;
@Service
@AllArgsConstructor
public class ArtisanServiceImpl implements ArtisanService {
    private final ArtisanRepository artisanRepository;
    private final ArtisanPhotoRepository artisanPhotoRepository;
    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final FileStorageService fileStorageService;


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


    @Transactional
    @Override
    public ArtisanProfileResponse createArtisanProfile(Long userId,
                                                       ArtisanProfileRequest request,
                                                       List<MultipartFile> profilePhotos) {
        Optional<Artisan> existingArtisan = artisanRepository.findByUserId(userId);
        if(existingArtisan.isPresent()){
            throw new RuntimeException("Artisan profile already exists for this user");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));

        // Create Artisan
        Artisan artisan = new Artisan();
        artisan.setUser(user);
        artisan.setDescription(request.getDescription());
        artisan.setYearsExperience(request.getYearsExperience());
        artisan.setCity(city);
        artisan.setCategories(categories);
        artisan.setVerified(false); // default
        artisan.setRatingAvg(0.0);
        // 5. Add photos
        if (profilePhotos != null && !profilePhotos.isEmpty()) {

            List<ArtisanPhoto> photos = new ArrayList<>();

            for (MultipartFile file : profilePhotos) {
                String imageUrl = fileStorageService.uploadFile(file);

                ArtisanPhoto photo = new ArtisanPhoto();
                photo.setArtisan(artisan);
                photo.setImageUrl(imageUrl);

                photos.add(photo);
            }

            artisan.setPhotos(photos);
        }


        Artisan savedArtisan = artisanRepository.save(artisan);
        return mapToDto(savedArtisan);
    }



}
