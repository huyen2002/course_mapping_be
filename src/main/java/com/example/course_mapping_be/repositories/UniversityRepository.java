package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}
