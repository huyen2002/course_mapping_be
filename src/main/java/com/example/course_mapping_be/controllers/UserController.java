package com.example.course_mapping_be.controllers;


import com.example.course_mapping_be.dtos.UserCreateDto;
import com.example.course_mapping_be.dtos.UserDto;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "users")
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping(path = "user/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


}
