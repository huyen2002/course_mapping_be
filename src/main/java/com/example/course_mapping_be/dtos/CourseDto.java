package com.example.course_mapping_be.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Code is required")
    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Language is required")
    @NotBlank(message = "Language is required")
    private String language;

    private String outline;
    
    private Date createdAt;
    private Date updatedAt;


}
