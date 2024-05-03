package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE BINARY u.username = :name", nativeQuery = true)
    Optional<User> findByUsername(String name);
}
