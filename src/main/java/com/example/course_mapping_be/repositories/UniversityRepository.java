package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long>, CustomUniversityRepository {

    Optional<University> findByUserId(Long userId);

    @Query(value = "SELECT * FROM university u WHERE BINARY u.code = :code", nativeQuery = true)
    Optional<University> findByCode(String code);

    @Query(value = "SELECT * FROM university u WHERE BINARY u.name = :name", nativeQuery = true)
    Optional<University> findByName(String name);
}
