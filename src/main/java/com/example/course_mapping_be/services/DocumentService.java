package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.CompareTwoDocumentsDto;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class DocumentService {

    public BaseResponse<Float> compareTwoDocuments(String document1, String document2) {
        BaseResponse<Float> baseResponse = new BaseResponse<>();
        CompareTwoDocumentsDto compareTwoDocumentsDto = new CompareTwoDocumentsDto(document1, document2);
        final String uri = "http://localhost:5000/api/compare_two_documents";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(uri, compareTwoDocumentsDto, String.class);
        float similarity = new JSONObject(result).getFloat("similarity");
        Float finalSimilarity = (float) (Math.round(similarity * 100 * 100.0) / 100.0);
        baseResponse.setData(finalSimilarity);
        baseResponse.success();
        return baseResponse;
    }
}
