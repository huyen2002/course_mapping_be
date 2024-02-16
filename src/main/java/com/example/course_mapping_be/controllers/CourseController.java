package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.CourseDto;
import com.example.course_mapping_be.services.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CourseController {
    private CourseService courseService;

    @PostMapping(path = "course/create")
    public ResponseEntity<BaseResponse<CourseDto>> create(@RequestBody CourseDto courseDto, HttpServletRequest request) throws Exception {
        BaseResponse<CourseDto> baseResponse = courseService.create(courseDto, request);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping(path = "course/{id}/update")
    public ResponseEntity<BaseResponse<CourseDto>> update(@PathVariable Long id, @RequestBody CourseDto courseDto, HttpServletRequest request) throws Exception {
        BaseResponse<CourseDto> baseResponse = courseService.update(id, courseDto, request);
        return ResponseEntity.ok(baseResponse);
    }
}
