package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.CourseDto;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.dtos.SearchCourseDto;
import com.example.course_mapping_be.services.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping(path = "courses/search")
    public ResponseEntity<BaseResponse<List<CourseDto>>> search(SearchCourseDto searchCourseDto, QueryParams params) throws Exception {
        BaseResponse<List<CourseDto>> baseResponse = courseService.search(searchCourseDto, params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "/university/{id}/courses/list")
    public ResponseEntity<BaseResponse<List<CourseDto>>> getListByUniversity(@PathVariable Long id) throws Exception {
        BaseResponse<List<CourseDto>> baseResponse = courseService.getListByUniversity(id);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping(path = "course/{id}/delete")
    public ResponseEntity<BaseResponse<Boolean>> deleteById(@PathVariable Long id) {
        BaseResponse<Boolean> baseResponse = courseService.deleteById(id);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "course/check_existed_by_code")
    public ResponseEntity<BaseResponse<Boolean>> existedByCode(@RequestParam String code) {
        BaseResponse<Boolean> baseResponse = courseService.existedByCode(code);
        return ResponseEntity.ok(baseResponse);
    }
}
