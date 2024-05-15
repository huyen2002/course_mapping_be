package com.example.course_mapping_be.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComparableProgramEducationDto {

    @NotNull(message = "First program id is required")
    private Long firstProgramId;

    @NotNull(message = "Second program id is required")
    private Long secondProgramId;


    private Float nameSimilarity;

    private Float introductionSimilarity;

    private Float outlineSimilarity;

    private String coursesMapping;


    public ComparableProgramEducationDto(Long firstProgramId, Long secondProgramId) {
        this.firstProgramId = firstProgramId;
        this.secondProgramId = secondProgramId;
    }
}
