package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.dtos.UniversityDto;
import com.example.course_mapping_be.services.UniversityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
@AllArgsConstructor
@RestController
public class UniversityController {
    private UniversityService universityService;

    @PutMapping(path = "university/update")
    public ResponseEntity<BaseResponse<UniversityDto>> update(@RequestBody UniversityDto universityDto, HttpServletRequest request) throws Exception {
        BaseResponse<UniversityDto> baseResponse = universityService.update(universityDto, request);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "universities/all")
    public ResponseEntity<BaseResponse<List<UniversityDto>>> getAll(QueryParams params) {
        BaseResponse<List<UniversityDto>> baseResponse = universityService.getAll(params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "university/{id}")
    public ResponseEntity<BaseResponse<UniversityDto>> getById(@PathVariable Long id) throws Exception {
        BaseResponse<UniversityDto> baseResponse = universityService.getById(id);
        return ResponseEntity.ok(baseResponse);
    }
}
