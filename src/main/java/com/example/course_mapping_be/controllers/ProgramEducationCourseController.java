package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.*;
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

    @GetMapping(path = "program_education_courses/search")
    public ResponseEntity<BaseResponse<List<ProgramEducationCourseDto>>> search(SearchCourseDto searchCourseDto, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationCourseDto>> baseResponse = programEducationCourseService.search(searchCourseDto, params);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping(path = "program_education_course/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(programEducationCourseService.deleteById(id));
    }

    @GetMapping(path = "compare_courses_of_program_educations/{id1}/and/{id2}")
    public ResponseEntity<BaseResponse<List<ComparedCourseDto>>> compareCoursesOfProgramEducations(@PathVariable Long id1, @PathVariable Long id2) throws Exception {
        BaseResponse<List<ComparedCourseDto>> baseResponse = programEducationCourseService.compareCoursesOfProgramEducations(id1, id2);
        return ResponseEntity.ok(baseResponse);
    }


}
