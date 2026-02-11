package com.javaapp.backend_7irfati.repository.artisan;

import com.javaapp.backend_7irfati.entity.ArtisanPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtisanPhotoRepository extends JpaRepository<ArtisanPhoto, Long> {

    List<ArtisanPhoto> findByArtisanId(Long artisanId);
}
