package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.CourseDto;
import com.example.course_mapping_be.dtos.SearchCourseDto;
import com.example.course_mapping_be.models.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Transactional
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);

    @Query("SELECT c FROM Course c WHERE c.university.id = ?1 order by c.updatedAt desc")
    Page<Course> findByUniversityId(Long id, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE " +
            "(:#{#searchCourseDto.name} IS NULL OR c.name LIKE %:#{#searchCourseDto.name}%) AND " +
            "(:#{#searchCourseDto.universityId} IS NULL OR c.university.id = :#{#searchCourseDto.universityId}) order by c.updatedAt desc")
    Page<Course> search(SearchCourseDto searchCourseDto, Pageable pageable);


    @Query("select c from Course c where c.university.id = ?1 ")
    List<Course> findAllByUniversityId(Long id);

    @Modifying
    @Query("delete  from Course c where c.university.id =?1")
    void deleteByUniversityId(Long id);
}
