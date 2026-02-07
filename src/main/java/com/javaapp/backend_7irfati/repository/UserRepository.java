package com.javaapp.backend_7irfati.repository;

import com.javaapp.backend_7irfati.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // Find a user by email (used for login, registration check)
    Optional<User> findByEmail(String email);

    // Check if a user exists by email
    boolean existsByEmail(String email);
    // Optional: fetch user with roles eagerly (avoiding lazy load issues)
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    // Optional: check existence ignoring case
    boolean existsByEmailIgnoreCase(String email);

    // Optional: custom query to find enabled users
    @Query("SELECT u FROM User u WHERE u.enabled = true AND u.email = :email")
    Optional<User> findEnabledUserByEmail(@Param("email") String email);

    // Optional: check if user has a role
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM User u JOIN u.roles r " +
            "WHERE u.email = :email AND r.name = :roleName")
    boolean hasRole(@Param("email") String email, @Param("roleName") String roleName);
}
