package com.example.course_mapping_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FilterProgramParams {
    private String country;
    private String language;
    private String major;
}
