package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.ProgramEducationCourseDto;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.services.ProgramEducationCourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProgramEducationCourseController {
    private ProgramEducationCourseService programEducationCourseService;

    @PostMapping(path = "program_education_course/create")
    public ResponseEntity<BaseResponse<ProgramEducationCourseDto>> create(@RequestBody ProgramEducationCourseDto programEducationCourseDto) throws Exception {
        BaseResponse<ProgramEducationCourseDto> baseResponse = programEducationCourseService.create(programEducationCourseDto);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "program_education/{id}/courses")
    public ResponseEntity<BaseResponse<List<ProgramEducationCourseDto>>> getAllCoursesByProgramEducationId(@PathVariable Long id, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationCourseDto>> baseResponse = programEducationCourseService.getAllCoursesByProgramEducationId(id, params);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping(path = "program_education_course/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(programEducationCourseService.deleteById(id));
    }
}
