package com.example.course_mapping_be.dtos;

import com.example.course_mapping_be.constraints.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserParams {
    public RoleType role;
    public Integer createMonth;
    public Integer createYear;
}
