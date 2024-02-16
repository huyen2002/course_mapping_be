package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {

    Optional<University> findByUserId(Long userId);

    Optional<University> findByCode(String code);
}
