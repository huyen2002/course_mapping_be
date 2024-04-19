package com.example.course_mapping_be;

import com.example.course_mapping_be.constraints.SourceLink;
import com.example.course_mapping_be.dtos.ProgramEducationDto;
import com.example.course_mapping_be.services.DocumentService;
import com.example.course_mapping_be.services.ProgramEducationService;
import com.example.course_mapping_be.services.ReadFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@SpringBootApplication
@EnableJpaAuditing
@RestController
public class CourseMappingBeApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(CourseMappingBeApplication.class, args);

        System.out.println("hello world, I have just started up");
        ReadFileService readFileService = new ReadFileService();
//        String text = readFileService.readData("https://firebasestorage.googleapis.com/v0/b/course-mapping-489fd.appspot.com/o/program_educations%2Ftest.pdf?alt=media&token=44fae099-1f65-41b3-b5bd-9778022b0886");
//        System.out.println(text);
//        DocumentService documentService = new DocumentService();
//        String vector = documentService.convertDocumentToVector(text).getData();
//        System.out.println(vector);

    }


    @GetMapping
    public String hello() {
        return "Hello world";
    }
}
