package com.example.course_mapping_be.dtos;

import com.example.course_mapping_be.constraints.FilterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilterParams {
    private FilterType filterType = FilterType.SIMILARITY_DESC;

}
