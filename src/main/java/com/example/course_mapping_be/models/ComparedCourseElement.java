package com.example.course_mapping_be.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ComparedCourseElement {
    private Long firstCourseId;
    private Long secondCourseId;
    private Float nameSimilarity;
    private Float outlineSimilarity;
    private Float similarity;
}
