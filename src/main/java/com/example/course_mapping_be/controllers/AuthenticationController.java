package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.constraints.Constants;
import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.UserCreateDto;
import com.example.course_mapping_be.dtos.UserDto;
import com.example.course_mapping_be.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserDto>> register(@RequestBody UserCreateDto userCreateDto) {
        BaseResponse<UserDto> baseResponse = authenticationService.register(userCreateDto);
        return ResponseEntity.ok(baseResponse);
    }
}
