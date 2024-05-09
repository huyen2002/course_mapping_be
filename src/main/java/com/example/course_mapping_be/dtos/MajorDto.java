package com.example.course_mapping_be.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorDto {
    Long id;
    @NotNull(message = "Code is required")
    @NotBlank(message = "Code is required")
    String code;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    String name;

    Integer numberOfProgramEducations;

    Boolean enabled;
}
