package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.CourseDto;
import com.example.course_mapping_be.dtos.SearchCourseDto;
import com.example.course_mapping_be.models.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);

    @Query("SELECT c FROM Course c WHERE c.university.id = ?1")
    Page<Course> findByUniversityId(Long id, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.university.id = ?1 AND c.name LIKE %:#{#searchCourseDto.name}%")
    Page<Course> search(Long universityId, SearchCourseDto searchCourseDto, Pageable pageable);
}
