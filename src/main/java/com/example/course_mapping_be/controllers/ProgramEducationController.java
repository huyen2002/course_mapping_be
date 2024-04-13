package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.ProgramEducationDto;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.dtos.SearchProgramDto;
import com.example.course_mapping_be.services.ProgramEducationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
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

    @GetMapping(path = "program_education/{id}/top_similar")
    public ResponseEntity<BaseResponse<List<Pair<ProgramEducationDto, Float>>>> getTopSimilar(@PathVariable Long id) throws Exception {
        BaseResponse<List<Pair<ProgramEducationDto, Float>>> baseResponse = programEducationService.getTopSimilar(id);
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

    @DeleteMapping(path = "program_education/delete/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(programEducationService.deleteById(id, request));
    }

    @GetMapping(path = "program_educations/all")
    public ResponseEntity<BaseResponse<List<ProgramEducationDto>>> getAll(QueryParams params) {
        BaseResponse<List<ProgramEducationDto>> baseResponse = programEducationService.getAll(params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "program_educations/search")
    public ResponseEntity<BaseResponse<List<ProgramEducationDto>>> search(SearchProgramDto searchProgramDto, QueryParams params) {
        BaseResponse<List<ProgramEducationDto>> baseResponse = programEducationService.search(searchProgramDto, params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "me/program_educations/all")
    public ResponseEntity<BaseResponse<List<ProgramEducationDto>>> getAllByUser(HttpServletRequest request, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationDto>> baseResponse = programEducationService.getAllByUser(request, params);
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping(path = "/compare_program_educations/{programId1}/and/{programId2}")
    public ResponseEntity<BaseResponse<Float>> compareTwoPrograms(@PathVariable Long programId1, @PathVariable Long programId2) throws Exception {
        BaseResponse<Float> baseResponse = programEducationService.compareTwoPrograms(programId1, programId2);
        return ResponseEntity.ok(baseResponse);
    }

}
