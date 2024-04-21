package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.FilterProgramParams;
import com.example.course_mapping_be.dtos.SearchProgramDto;
import com.example.course_mapping_be.models.ProgramEducation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramEducationRepository extends JpaRepository<ProgramEducation, Long> {
    Page<ProgramEducation> findAllByUniversityId(Long id, Pageable pageable);

    Page<ProgramEducation> findAllByMajorId(Long id, Pageable pageable);


    @Query
            ("SELECT p FROM ProgramEducation p WHERE " +
                    "(:#{#searchProgramDto.name} IS NULL OR p.name LIKE %:#{#searchProgramDto.name}%) AND " +
                    "(:#{#searchProgramDto.majorCode} IS NULL OR p.major.code = :#{#searchProgramDto.majorCode}) AND " +
                    "(:#{#searchProgramDto.levelOfEducation} is NULL OR p.levelOfEducation = :#{#searchProgramDto.levelOfEducation}) AND " +
                    "(:#{#searchProgramDto.universityId} is NULL OR p.university.id = :#{#searchProgramDto.universityId}) AND " +
                    "(:#{#searchProgramDto.status} is NULL OR (p.endYear > YEAR(CURRENT_DATE ) OR  p.endYear is null  AND :#{#searchProgramDto.status} = 'ACTIVE') OR (p.endYear < YEAR(CURRENT_DATE) AND :#{#searchProgramDto.status} = 'INACTIVE'))"
            )
    Page<ProgramEducation> searchPrograms(SearchProgramDto searchProgramDto, Pageable pageable);

    @Query("SELECT p FROM ProgramEducation p WHERE p.university.code = :universityCode AND p.code = :programEducationCode")
    ProgramEducation findByUniversityCodeAndProgramEducationCode(String universityCode, String programEducationCode);


    // select all program by filter params: if one field is null, it will not be used to filter. If field contains "!" at the beginning, it will be used to filter not equal
    // if field don't contain "!", it will be used to filter equal
    
    List<ProgramEducation> findAllByFilterParams(FilterProgramParams filterProgramParams);
}
