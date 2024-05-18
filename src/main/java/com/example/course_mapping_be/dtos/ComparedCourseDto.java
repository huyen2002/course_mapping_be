package com.example.course_mapping_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ComparedCourseDto {
    private CourseDto firstCourse;
    private CourseDto secondCourse;
    private Float nameSimilarity;
    private Float outlineSimilarity;
    private Float similarity;

}
