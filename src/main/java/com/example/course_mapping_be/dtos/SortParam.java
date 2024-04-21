package com.example.course_mapping_be.dtos;

import com.example.course_mapping_be.constraints.SortType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SortParam {
    private SortType sortType = SortType.SIMILARITY_DESC;

}
