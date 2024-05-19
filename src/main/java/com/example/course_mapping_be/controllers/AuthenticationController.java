package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.LoginRequestDto;
import com.example.course_mapping_be.dtos.UserCreateDto;
import com.example.course_mapping_be.dtos.UserDto;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import com.example.course_mapping_be.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserDto>> register(@RequestBody UserCreateDto userCreateDto) throws Exception {
        BaseResponse<UserDto> baseResponse = authenticationService.register(userCreateDto);
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<BaseResponse<Map<String, Object>>> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        BaseResponse<Map<String, Object>> baseResponse = authenticationService.login(loginRequestDto);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<BaseResponse<UserDto>> me(HttpServletRequest request) throws Exception {
        BaseResponse<UserDto> baseResponse = authenticationService.me(request);
        return ResponseEntity.ok(baseResponse);
    }


}
