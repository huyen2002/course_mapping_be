package com.example.course_mapping_be.dtos;

import com.example.course_mapping_be.constraints.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDto {
    @NotBlank(message = "Email is required")
    @NotNull(message = "Email is required")
    @Email(message = "Email is invalid")
    String email;

    @NotBlank(message = "Password is required")
    @NotNull(message = "Password is required")
    String password;

    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    String name;

    @NotNull(message = "Role is required")
    RoleType role;
}
