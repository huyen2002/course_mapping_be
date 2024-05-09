package com.example.course_mapping_be.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilterUniversityParams {
    private String name;
    private String country;
    private Boolean enabled;
}
