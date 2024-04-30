package com.example.course_mapping_be.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;


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
    private Float durationYear;

    @NotNull(message = "Level of education is required")
    private String levelOfEducation;

    private Integer numCredits;

    private String outline;

    private Integer startYear;
    private Integer endYear;

    private String sourceLinks;

    private Date createdAt;
    private Date updatedAt;

    private Long universityId;

    private UniversityDto university;

    @NotNull(message = "Major is required")
    private Long majorId;

    private MajorDto major;
}
