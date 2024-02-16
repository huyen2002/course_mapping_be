package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.CourseDto;
import com.example.course_mapping_be.models.Course;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.repositories.CourseRepository;
import com.example.course_mapping_be.repositories.UniversityRepository;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private ModelMapper modelMapper;

    private JsonWebTokenProvider tokenProvider;
    private final UniversityRepository universityRepository;

    public BaseResponse<CourseDto> create(CourseDto courseDto, HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));

        BaseResponse<CourseDto> baseResponse = new BaseResponse<>();
        if (courseRepository.existsByCode(courseDto.getCode())) {
            throw new Exception("Code is already used");
        }
        Course course = Course.builder()
                .name(courseDto.getName())
                .code(courseDto.getCode())
                .language(courseDto.getLanguage())
                .outline(courseDto.getOutline())
                .university(university).build();

        courseRepository.save(course);
        baseResponse.setData(modelMapper.map(course, CourseDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<CourseDto> update(Long id, CourseDto courseDto, HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        Course course = courseRepository.findById(id).orElseThrow(() -> new Exception("Course is not found"));
        if (!Objects.equals(course.getUniversity().getId(), university.getId())) {
            throw new Exception("You are not allowed to update this course");
        }
        BaseResponse<CourseDto> baseResponse = new BaseResponse<>();
        if (courseDto.getName() != null) {
            course.setName(courseDto.getName());
        }
        if (courseDto.getCode() != null) {
            if (courseRepository.existsByCode(courseDto.getCode())) {
                throw new Exception("Code is already used");
            }
            course.setCode(courseDto.getCode());
        }
        if (courseDto.getLanguage() != null) {
            course.setLanguage(courseDto.getLanguage());
        }
        if (courseDto.getOutline() != null) {
            course.setOutline(courseDto.getOutline());
        }
        courseRepository.save(course);
        baseResponse.setData(modelMapper.map(course, CourseDto.class));
        baseResponse.success();
        return baseResponse;
    }
}
