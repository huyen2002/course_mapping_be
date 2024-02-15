package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.ProgramEducationDto;
import com.example.course_mapping_be.services.ProgramEducationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor

public class ProgramEducationController {
    private ProgramEducationService programEducationService;

    @PostMapping(path = "program_education/create")
    public ResponseEntity<BaseResponse<ProgramEducationDto>> create(@RequestBody ProgramEducationDto programEducationDto, HttpServletRequest request) throws Exception {
        BaseResponse<ProgramEducationDto> baseResponse = programEducationService.create(programEducationDto, request);
        return ResponseEntity.ok(baseResponse);
    }
}
