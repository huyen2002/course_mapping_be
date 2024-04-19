package com.example.course_mapping_be.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCourseDto {
    public String name;
    public Long universityId;
    public Long programEducationId;
}
