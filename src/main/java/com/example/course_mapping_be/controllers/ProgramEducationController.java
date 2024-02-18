package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.ProgramEducationDto;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.services.ProgramEducationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor

public class ProgramEducationController {
    private ProgramEducationService programEducationService;

    @PostMapping(path = "program_education/create")
    public ResponseEntity<BaseResponse<ProgramEducationDto>> create(@RequestBody ProgramEducationDto programEducationDto, HttpServletRequest request) throws Exception {
        BaseResponse<ProgramEducationDto> baseResponse = programEducationService.create(programEducationDto, request);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "university/{id}/program_educations/all")
    public ResponseEntity<BaseResponse<List<ProgramEducationDto>>> getAllByUniversityId(@PathVariable Long id, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationDto>> baseResponse = programEducationService.getAllByUniversityId(id, params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "major/{id}/program_educations/all")
    public ResponseEntity<BaseResponse<List<ProgramEducationDto>>> getAllByMajorId(@PathVariable Long id, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationDto>> baseResponse = programEducationService.getAllByMajorId(id, params);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping(path = "program_education/update/{id}")
    public ResponseEntity<BaseResponse<ProgramEducationDto>> update(@PathVariable Long id, @RequestBody ProgramEducationDto programEducationDto, HttpServletRequest request) throws Exception {
        BaseResponse<ProgramEducationDto> baseResponse = programEducationService.update(id, programEducationDto, request);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "program_education/{id}")
    public ResponseEntity<BaseResponse<ProgramEducationDto>> getById(@PathVariable Long id) throws Exception {
        BaseResponse<ProgramEducationDto> baseResponse = programEducationService.getById(id);
        return ResponseEntity.ok(baseResponse);
    }
}
