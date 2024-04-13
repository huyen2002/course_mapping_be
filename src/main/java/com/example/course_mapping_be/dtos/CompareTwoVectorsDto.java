package com.example.course_mapping_be.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
public class CompareTwoVectorsDto {
    private List<Object> vector1;
    private List<Object> vector2;
    
}
