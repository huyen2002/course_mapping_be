package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.SearchProgramDto;
import com.example.course_mapping_be.models.ProgramEducation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProgramEducationRepository extends JpaRepository<ProgramEducation, Long> {
    Page<ProgramEducation> findAllByUniversityId(Long id, Pageable pageable);

    Page<ProgramEducation> findAllByMajorId(Long id, Pageable pageable);


    @Query
            ("SELECT p FROM ProgramEducation p WHERE " +
                    "(:#{#searchProgramDto.name} IS NULL OR p.name LIKE %:#{#searchProgramDto.name}%) AND " +
                    "(:#{#searchProgramDto.majorCode} IS NULL OR p.major.code = :#{#searchProgramDto.majorCode}) AND " +
                    "(:#{#searchProgramDto.levelOfEducation} is NULL OR p.level_of_education = :#{#searchProgramDto.levelOfEducation}) AND " +
                    "(:#{#searchProgramDto.status} is NULL OR (p.end_year > YEAR(CURRENT_DATE) AND :#{#searchProgramDto.status} = 'ACTIVE') OR (p.end_year < YEAR(CURRENT_DATE) AND :#{#searchProgramDto.status} = 'INACTIVE'))"
            )
    Page<ProgramEducation> searchPrograms(SearchProgramDto searchProgramDto, Pageable pageable);

    @Query("SELECT p FROM ProgramEducation p WHERE p.university.code = :universityCode AND p.code = :programEducationCode")
    ProgramEducation findByUniversityCodeAndProgramEducationCode(String universityCode, String programEducationCode);
}
