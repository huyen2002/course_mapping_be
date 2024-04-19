package com.example.course_mapping_be.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgramEducationCourseDto {
    Long id;

    @NotNull(message = "Program education id is required")
    Long programEducationId;

    @NotNull(message = "Course id is required")
    Long courseId;

    CourseDto course;

    Boolean compulsory;

    @NotNull(message = "Number of credits is required")
    Integer numCredits;

}
