package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.FilterUniversityParams;
import com.example.course_mapping_be.models.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUniversityRepository {
    Page<University> filterUniversities(FilterUniversityParams filterParams, Pageable pageable);
}
