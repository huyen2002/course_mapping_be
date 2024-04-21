package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.ComparableProgramEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ComparableProgramEducationRepository extends JpaRepository<ComparableProgramEducation, Long> {

    @Query("SELECT c FROM ComparableProgramEducation c WHERE c.firstProgramId = ?1 AND c.secondProgramId = ?2 OR c.firstProgramId = ?2 AND c.secondProgramId = ?1")
    Optional<ComparableProgramEducation> findByFirstProgramIdAndSecondProgramId(Long firstProgramId, Long secondProgramId);

    @Modifying
    @Query("UPDATE ComparableProgramEducation c SET c.status = 'NEEDS_UPDATED' WHERE c.firstProgramId = ?1 OR c.secondProgramId = ?1")
    void updateStatusByProgramId(Long id);
}
