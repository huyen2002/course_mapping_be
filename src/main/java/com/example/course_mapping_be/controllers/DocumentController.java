package com.example.course_mapping_be.controllers;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.CompareTwoDocumentsDto;
import com.example.course_mapping_be.dtos.DocumentDto;
import com.example.course_mapping_be.dtos.UrlDto;
import com.example.course_mapping_be.services.DocumentService;
import com.example.course_mapping_be.services.ReadFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@RestController
@AllArgsConstructor
public class DocumentController {
    private DocumentService documentService;
    private ReadFileService readFileService;

    @PostMapping(path = "/compare_two_documents")
    public ResponseEntity<BaseResponse<Float>> compareTwoDocuments(@RequestBody CompareTwoDocumentsDto compareTwoDocumentsDto) {
        BaseResponse<Float> baseResponse = documentService.compareTwoDocuments(compareTwoDocumentsDto.getDocument_1(), compareTwoDocumentsDto.getDocument_2());
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping(path = "/infer_vector")
    public ResponseEntity<BaseResponse<String>> convertDocumentToVector(@RequestBody UrlDto url) throws MalformedURLException, UnsupportedEncodingException {
        String text = readFileService.readData(url.getUrl());
        BaseResponse<String> baseResponse = documentService.convertDocumentToVector(text);
        return ResponseEntity.ok(baseResponse);
    }
}
