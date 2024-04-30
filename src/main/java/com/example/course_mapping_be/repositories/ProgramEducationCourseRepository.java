package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.dtos.SearchCourseDto;
import com.example.course_mapping_be.models.Course;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.ProgramEducationCourse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ProgramEducationCourseRepository extends JpaRepository<ProgramEducationCourse, Long> {

    @Query("SELECT pec FROM ProgramEducationCourse pec WHERE pec.programEducation.id = ?1 AND pec.course.id = ?2")
    Optional<ProgramEducationCourse> findByProgramEducationIdAndCourseId(Long program_education_id, Long course_id);

    @Query(
            "SELECT pec FROM ProgramEducationCourse pec " +
                    "WHERE " +
                    "(:#{#searchCourseDto.name} IS NULL OR pec.course.name LIKE %:#{#searchCourseDto.name}%) AND " +
                    "(:#{#searchCourseDto.universityId} IS NULL OR pec.programEducation.university.id = :#{#searchCourseDto.universityId}) AND " +
                    "(:#{#searchCourseDto.programEducationId} IS NULL OR pec.programEducation.id = :#{#searchCourseDto.programEducationId})" +
                    "order by pec.updatedAt desc"
    )
    Page<ProgramEducationCourse> searchCourses(SearchCourseDto searchCourseDto, Pageable pageable);

    @Query("SELECT pec FROM ProgramEducationCourse pec WHERE pec.programEducation.id = ?1 order by pec.updatedAt desc")
    List<ProgramEducationCourse> findAllByProgramEducationId(Long id);

    @Query("SELECT pec FROM ProgramEducationCourse pec WHERE pec.course.id = ?1")
    List<ProgramEducationCourse> findAllByCourseId(Long id);

    @Modifying
    @Query("DELETE FROM ProgramEducationCourse pec WHERE pec.course.id = ?1")
    void deleteByCourseId(Long id);


    @Modifying
    @Query("DELETE FROM ProgramEducationCourse pec WHERE pec.programEducation.id = ?1")
    void deleteByProgramId(Long id);
}
