package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.FilterProgramParams;
import com.example.course_mapping_be.models.ProgramEducation;

import java.util.List;

public interface CustomProgramEducationRepository {
    List<ProgramEducation> findAllByFilterParams(FilterProgramParams filterProgramParams);
}
