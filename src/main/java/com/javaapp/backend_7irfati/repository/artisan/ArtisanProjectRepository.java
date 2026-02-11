package com.javaapp.backend_7irfati.repository.artisan;

import com.javaapp.backend_7irfati.entity.ArtisanProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtisanProjectRepository extends JpaRepository<ArtisanProject, Long> {

    List<ArtisanProject> findByArtisanId(Long artisanId);
}
