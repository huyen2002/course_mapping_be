package com.example.course_mapping_be.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UniversityDto {
    Long id;

    UserDto user;

    @NotNull(message = "Code is required")
    @NotBlank(message = "Code is required")
    String code;

    String feature;
    String introduction;
    
    AddressDto address;
}
