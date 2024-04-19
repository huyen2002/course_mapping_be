package com.example.course_mapping_be.dtos;

import com.example.course_mapping_be.constraints.LevelEducationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProgramDto {
    public String name;
    public String majorCode;
    public LevelEducationType levelOfEducation;
    public String status;
    
}
