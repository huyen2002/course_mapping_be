package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.FilterUniversityParams;
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

@AllArgsConstructor
@RestController
public class UniversityController {
    private UniversityService universityService;

    @PutMapping(path = "university/update")
    public ResponseEntity<BaseResponse<UniversityDto>> update(@RequestBody UniversityDto universityDto, HttpServletRequest request) throws Exception {
        BaseResponse<UniversityDto> baseResponse = universityService.update(universityDto, request);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping(path = "university/{id}/update")
    public ResponseEntity<BaseResponse<UniversityDto>> updateById(@PathVariable Long id, @RequestBody UniversityDto universityDto) throws Exception {
        BaseResponse<UniversityDto> baseResponse = universityService.updateByUniversityId(id, universityDto);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "universities/all")
    public ResponseEntity<BaseResponse<List<UniversityDto>>> getAll(QueryParams params) {
        BaseResponse<List<UniversityDto>> baseResponse = universityService.getAll(params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "universities/search")
    public ResponseEntity<BaseResponse<List<UniversityDto>>> search(FilterUniversityParams filterParams, QueryParams params) {
        BaseResponse<List<UniversityDto>> baseResponse = universityService.search(filterParams, params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "universities/list")
    public ResponseEntity<BaseResponse<List<UniversityDto>>> getList() {
        BaseResponse<List<UniversityDto>> baseResponse = universityService.getList();
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "university/{id}")
    public ResponseEntity<BaseResponse<UniversityDto>> getById(@PathVariable Long id) throws Exception {
        BaseResponse<UniversityDto> baseResponse = universityService.getById(id);
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping(path = "university/create")
    public ResponseEntity<BaseResponse<UniversityDto>> create(@RequestBody UniversityDto universityDto) {
        BaseResponse<UniversityDto> baseResponse = universityService.create(universityDto);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "university/me")
    public ResponseEntity<BaseResponse<UniversityDto>> getByUser(HttpServletRequest request) throws Exception {
        BaseResponse<UniversityDto> baseResponse = universityService.getByUser(request);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping(path = "university/{id}/delete")
    public ResponseEntity<BaseResponse<Boolean>> delete(@PathVariable Long id) throws Exception {
        BaseResponse<Boolean> baseResponse = universityService.deleteById(id);
        return ResponseEntity.ok(baseResponse);
    }
}
