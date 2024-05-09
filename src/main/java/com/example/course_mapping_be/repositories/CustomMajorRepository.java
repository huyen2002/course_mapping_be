package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.SearchMajorDto;
import com.example.course_mapping_be.models.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMajorRepository {
    Page<Major> searchMajors(SearchMajorDto searchMajorDto, Pageable pageable);
}
