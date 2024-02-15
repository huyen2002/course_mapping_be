package com.example.course_mapping_be.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ProgramEducationDto {

    private Long id;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Language is required")
    @NotBlank(message = "Language is required")
    private String language;

    private String introduction;

    private Integer duration_year;

    @NotNull(message = "Level of education is required")
    private String level_of_education;

    private Integer num_credits;

    private String outline;

    private UniversityDto university;

    @NotNull(message = "Major is required")
    private Long majorId;

    private MajorDto major;
}
