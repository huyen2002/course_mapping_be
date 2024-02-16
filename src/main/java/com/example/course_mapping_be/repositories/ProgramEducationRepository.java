package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.ProgramEducation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramEducationRepository extends JpaRepository<ProgramEducation, Long> {
    Page<ProgramEducation> findAllByUniversityId(Long id, Pageable pageable);


    Page<ProgramEducation> findAllByMajorId(Long id, Pageable pageable);
}
