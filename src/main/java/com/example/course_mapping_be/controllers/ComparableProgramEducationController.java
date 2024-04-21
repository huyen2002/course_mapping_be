package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.ComparableProgramEducationDto;
import com.example.course_mapping_be.services.ComparableProgramEducationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ComparableProgramEducationController {
    private ComparableProgramEducationService comparableProgramEducationService;


    @GetMapping("/compare_programs/{firstProgramId}/and/{secondProgramId}")
    public ResponseEntity<BaseResponse<ComparableProgramEducationDto>> getComparableProgramEducation(@PathVariable Long firstProgramId, @PathVariable Long secondProgramId) throws Exception {
        BaseResponse<ComparableProgramEducationDto> baseResponse = comparableProgramEducationService.getComparableProgramEducation(firstProgramId, secondProgramId);
        return ResponseEntity.ok(baseResponse);
    }
}
