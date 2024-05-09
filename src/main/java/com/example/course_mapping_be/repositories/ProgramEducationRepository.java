package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.dtos.SearchProgramDto;
import com.example.course_mapping_be.models.ProgramEducation;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Transactional
public interface ProgramEducationRepository extends JpaRepository<ProgramEducation, Long>, CustomProgramEducationRepository {

    @Query("SELECT p FROM ProgramEducation p WHERE p.university.id = :id order by p.updatedAt desc")
    Page<ProgramEducation> findAllByUniversityId(Long id, Pageable pageable);

    @Query("SELECT p FROM ProgramEducation p WHERE p.university.id = :id")
    List<ProgramEducation> findByUniversityId(Long id);

    @Query("SELECT p FROM ProgramEducation p WHERE p.major.id = :id order by p.updatedAt desc")
    Page<ProgramEducation> findAllByMajorId(Long id, Pageable pageable);

    @Modifying
    @Query("Delete  from ProgramEducation p where p.university.id = ?1")
    void deleteByUniversityId(Long id);

    @Modifying
    @Query("Update ProgramEducation p set p.enabled = ?2 where p.university.id = ?1")
    void updateEnabledByUniversityId(Long id, Boolean enabled);


    @Modifying
    @Query("Update ProgramEducation p set p.enabled = ?2 where p.major.id = ?1")
    void updateEnabledByMajorId(Long id, Boolean enabled);


//    boolean existsByCode(String code);
}
