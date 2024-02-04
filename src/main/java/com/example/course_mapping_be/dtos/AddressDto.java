package com.example.course_mapping_be.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDto {

    @NotNull(message = "Detail is required")
    @NotBlank(message = "Detail is required")
    String detail;

    @NotNull(message = "District is required")
    @NotBlank(message = "District is required")
    String district;

    @NotNull(message = "City is required")
    @NotBlank(message = "City is required")
    String city;

    @NotNull(message = "Country is required")
    @NotBlank(message = "Country is required")
    String country;
}
