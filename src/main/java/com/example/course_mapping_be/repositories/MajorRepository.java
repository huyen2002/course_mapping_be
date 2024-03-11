package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.SearchMajorDto;
import com.example.course_mapping_be.models.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Object> findByCode(String code);

    @Query("SELECT m FROM Major m WHERE " +
            "(:#{#searchMajorDto.name} IS NULL OR m.name LIKE %:#{#searchMajorDto.name}%) AND " +
            "(:#{#searchMajorDto.code} IS NULL OR m.code LIKE :#{#searchMajorDto.code})")
    Page<Major> searchMajors(SearchMajorDto searchMajorDto, Pageable pageable);
}
