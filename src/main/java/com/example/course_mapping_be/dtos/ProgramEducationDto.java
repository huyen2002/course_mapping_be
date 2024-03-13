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

    @NotNull(message = "Code is required")
    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Language is required")
    @NotBlank(message = "Language is required")
    private String language;

    private String introduction;

    @NotNull(message = "Duration year is required")
    private Float duration_year;

    @NotNull(message = "Level of education is required")
    private String level_of_education;

    private Integer num_credits;

    private String outline;

    @NotNull(message = "Start year is required")
    private Integer start_year;

    @NotNull(message = "End year is required")
    private Integer end_year;

    private UniversityDto university;

    @NotNull(message = "Major is required")
    private Long majorId;

    private MajorDto major;
}
