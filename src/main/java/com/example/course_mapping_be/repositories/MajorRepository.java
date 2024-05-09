package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long>, CustomMajorRepository {
    Optional<Object> findByCode(String code);

}
