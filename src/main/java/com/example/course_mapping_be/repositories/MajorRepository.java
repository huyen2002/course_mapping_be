package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long>, CustomMajorRepository {
    Optional<Object> findByCode(String code);

    @Query("SELECT m FROM Major m WHERE m.enabled = true order by m.name asc")
    List<Major> findAllEnabledMajors();
}
