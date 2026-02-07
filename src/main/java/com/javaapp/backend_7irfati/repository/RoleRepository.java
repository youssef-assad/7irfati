package com.javaapp.backend_7irfati.repository;

import com.javaapp.backend_7irfati.entity.Role;
import com.javaapp.backend_7irfati.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByName(RoleName name);



    // Check if a role exists by name
    boolean existsByName(String name);

    //find role ignoring case
    Optional<Role> findByNameIgnoreCase(String name);
}