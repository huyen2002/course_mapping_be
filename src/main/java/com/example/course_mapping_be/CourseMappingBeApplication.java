package com.example.course_mapping_be;

import com.example.course_mapping_be.dtos.ProgramEducationDto;
import com.example.course_mapping_be.services.ProgramEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaAuditing
@RestController
public class CourseMappingBeApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(CourseMappingBeApplication.class, args);

        System.out.println("hello world, I have just started up");
    }

    @GetMapping
    public String hello() {
        return "Hello world";
    }
}
