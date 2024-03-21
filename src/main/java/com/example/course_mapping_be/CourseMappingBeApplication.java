package com.example.course_mapping_be;

import com.example.course_mapping_be.constraints.SourceLink;
import com.example.course_mapping_be.dtos.ProgramEducationDto;
import com.example.course_mapping_be.services.ProgramEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@RestController
public class CourseMappingBeApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(CourseMappingBeApplication.class, args);

        System.out.println("hello world, I have just started up");
        List<SourceLink> sourceLinks = new ArrayList<>();

        SourceLink link1 = (new SourceLink("Chương trình đào tạo ngành công nghệ thông tin chuẩn - UET", "https://uet.vnu.edu.vn/chuong-trinh-dao-tao-nganh-cong-nghe-thong-tin-4/"));
        SourceLink link2 = (new SourceLink("Nội dung chương trình đào tạo ngành công nghệ thông tin chuẩn - UET", "https://uet.vnu.edu.vn/chuong-trinh-dao-tao-nganh-cong-nghe-thong-tin-10/"));

        sourceLinks.add(link1);
        sourceLinks.add(link2);
        System.out.println(sourceLinks);
    }


    @GetMapping
    public String hello() {
        return "Hello world";
    }
}
