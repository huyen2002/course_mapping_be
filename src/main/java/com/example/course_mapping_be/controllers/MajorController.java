package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.MajorDto;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.dtos.SearchMajorDto;
import com.example.course_mapping_be.models.Major;
import com.example.course_mapping_be.services.MajorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class MajorController {

    private MajorService majorService;

    @PostMapping("major/create")
    public ResponseEntity<BaseResponse<MajorDto>> create(@RequestBody MajorDto majorDto) throws Exception {
        BaseResponse<MajorDto> baseResponse = majorService.create(majorDto);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "majors/all")
    public ResponseEntity<BaseResponse<List<MajorDto>>> getPage(QueryParams params) {
        BaseResponse<List<MajorDto>> baseResponse = majorService.getPage(params);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "majors/list")
    public ResponseEntity<BaseResponse<List<MajorDto>>> getAll() {
        BaseResponse<List<MajorDto>> baseResponse = majorService.getAll();
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping(path = "majors/search")
    public ResponseEntity<BaseResponse<List<MajorDto>>> searchMajors(SearchMajorDto searchMajorDto, QueryParams params) {
        BaseResponse<List<MajorDto>> baseResponse = majorService.searchMajors(searchMajorDto, params);
        return ResponseEntity.ok(baseResponse);
    }


    @PutMapping(path = "major/update/{id}")
    public ResponseEntity<BaseResponse<MajorDto>> update(@PathVariable Long id, @RequestBody MajorDto majorDto) throws Exception {
        BaseResponse<MajorDto> baseResponse = majorService.update(id, majorDto);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping(path = "major/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) throws Exception {

        return ResponseEntity.ok(Map.of("success", majorService.deleteById(id)));
    }
}
