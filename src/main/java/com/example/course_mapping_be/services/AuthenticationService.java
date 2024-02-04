package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.RoleType;
import com.example.course_mapping_be.dtos.UserCreateDto;
import com.example.course_mapping_be.dtos.UserDto;
import com.example.course_mapping_be.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private final UserService userService;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final UniversityService universityService;

    public AuthenticationService(UserService userService, ModelMapper modelMapper, UniversityService universityService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.universityService = universityService;
    }

    public UserDto register(UserCreateDto userCreateDto) {
        User user = userService.createUser(userCreateDto);

        if (user.getRole() == RoleType.UNIVERSITY) {
            universityService.createEmptyUniversity(user);
        }

        return modelMapper.map(user, UserDto.class);
    }
}
