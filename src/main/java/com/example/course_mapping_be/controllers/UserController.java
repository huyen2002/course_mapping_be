package com.example.course_mapping_be.controllers;


import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.dtos.UserCreateDto;
import com.example.course_mapping_be.dtos.UserDto;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "users/all")
    public ResponseEntity<BaseResponse<List<UserDto>>> getAll(QueryParams params) {
        BaseResponse<List<UserDto>> baseResponse = userService.getAll(params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "user/{id}")
    public ResponseEntity<BaseResponse<UserDto>> getById(@PathVariable Long id) {
        BaseResponse<UserDto> baseResponse = userService.getUserById(id);
        return ResponseEntity.ok(baseResponse);
    }


}
