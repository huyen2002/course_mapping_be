package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.CompareTwoDocumentsDto;
import com.example.course_mapping_be.dtos.DocumentDto;
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

    public BaseResponse<String> convertDocumentToVector(String data) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        final String uri = "http://localhost:5000/api/infer_vector";
        RestTemplate restTemplate = new RestTemplate();
        DocumentDto document = new DocumentDto(data);
        String result = restTemplate.postForObject(uri, document, String.class);
//        System.out.println(result);
//        //get type of result
//        System.out.println(result.getClass());
        baseResponse.setData(result);
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<String> convertDocumentToVectorDbow(String data) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        final String uri = "http://localhost:5000/api/infer_vector_dbow";
        RestTemplate restTemplate = new RestTemplate();
        DocumentDto document = new DocumentDto(data);
        String result = restTemplate.postForObject(uri, document, String.class);
        baseResponse.setData(result);
        baseResponse.success();
        return baseResponse;
    }
}
