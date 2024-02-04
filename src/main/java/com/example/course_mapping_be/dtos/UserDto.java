package com.example.course_mapping_be.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String email;
    String name;
    Date create_at;
    Date update_at;
    String role;
    boolean is_active;


}
