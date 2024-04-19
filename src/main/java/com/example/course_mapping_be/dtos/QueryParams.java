package com.example.course_mapping_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryParams {
    private Integer page = 0;
    private Integer size = 5;
}
