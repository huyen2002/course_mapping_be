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
    private CourseDto course1;
    private CourseDto course2;
    private Float similarity;


}
