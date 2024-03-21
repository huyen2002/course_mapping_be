package com.example.course_mapping_be.constraints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class SourceLink {
    private String name;
    private String link;
}
