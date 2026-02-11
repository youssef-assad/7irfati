package com.javaapp.backend_7irfati.repository.artisan;

import com.javaapp.backend_7irfati.entity.ProjectPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectPhotoRepository extends JpaRepository<ProjectPhoto, Long> {

    List<ProjectPhoto> findByProjectId(Long projectId);
}
