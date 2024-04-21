package com.example.course_mapping_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FilterProgramParams {
    private String nation;
    private String language;
    private String majorType;
}
