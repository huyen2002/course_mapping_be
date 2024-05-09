package com.example.course_mapping_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchMajorDto {
    public String name;
    public String code;
    public Boolean enabled;
}
