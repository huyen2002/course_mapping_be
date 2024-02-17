package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.models.Course;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.ProgramEducationCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProgramEducationCourseRepository extends JpaRepository<ProgramEducationCourse, Long> {

    @Query("SELECT pec FROM ProgramEducationCourse pec WHERE pec.program_education.id = ?1 AND pec.course.id = ?2")
    Optional<Object> findByProgramEducationIdAndCourseId(Long program_education_id, Long course_id);

    @Query("SELECT pec FROM ProgramEducationCourse pec WHERE pec.program_education.id = ?1")
    Page<ProgramEducationCourse> findAllByProgramEducationId(Long id, Pageable pageable);
}
