package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.CompareTwoDocumentsDto;
import com.example.course_mapping_be.services.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DocumentController {
    private DocumentService documentService;

    @PostMapping(path = "/compare_two_documents")
    public ResponseEntity<BaseResponse<Float>> compareTwoDocuments(@RequestBody CompareTwoDocumentsDto compareTwoDocumentsDto) {
        BaseResponse<Float> baseResponse = documentService.compareTwoDocuments(compareTwoDocumentsDto.getDocument_1(), compareTwoDocumentsDto.getDocument_2());
        return ResponseEntity.ok(baseResponse);
    }
}
