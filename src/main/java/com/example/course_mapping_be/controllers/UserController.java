package com.example.course_mapping_be.controllers;


import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping(path = "admin/users/all")
    public ResponseEntity<BaseResponse<List<UserDto>>> getAll(QueryParams params) {
        BaseResponse<List<UserDto>> baseResponse = userService.getAll(params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "user/{id}")
    public ResponseEntity<BaseResponse<UserDto>> getById(@PathVariable Long id) {
        BaseResponse<UserDto> baseResponse = userService.getUserById(id);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping(path = "user/{id}/update")
    public ResponseEntity<BaseResponse<UserDto>> update(@PathVariable Long id, @RequestBody UserDto userDto) throws Exception {
        BaseResponse<UserDto> baseResponse = userService.update(id, userDto);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "users/search")
    public ResponseEntity<BaseResponse<List<UserDto>>> search(SearchUserParams searchUserParams, QueryParams params) {
        BaseResponse<List<UserDto>> baseResponse = userService.search(searchUserParams, params);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping(path = "user/{id}/change_password")
    public ResponseEntity<BaseResponse<Boolean>> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
        BaseResponse<Boolean> baseResponse = userService.changePassword(id, changePasswordDto);
        return ResponseEntity.ok(baseResponse);
    }

}
