package com.example.course_mapping_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComparedCourseDto {
    private Long id;
    private CourseDto firstCourse;
    private CourseDto secondCourse;
    private Float similarity;

    public ComparedCourseDto(CourseDto firstCourse, CourseDto secondCourse, Float similarity) {
        this.firstCourse = firstCourse;
        this.secondCourse = secondCourse;
        this.similarity = similarity;
    }
}
