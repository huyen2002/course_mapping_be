package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.FilterProgramParams;
import com.example.course_mapping_be.dtos.SearchProgramDto;
import com.example.course_mapping_be.models.ProgramEducation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomProgramEducationRepository {
    List<ProgramEducation> findAllByFilterParams(FilterProgramParams filterProgramParams);

    Page<ProgramEducation> searchPrograms(SearchProgramDto searchProgramDto, Pageable pageable);
}
