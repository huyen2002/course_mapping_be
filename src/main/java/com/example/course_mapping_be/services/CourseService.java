package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.Course;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.repositories.CourseRepository;
import com.example.course_mapping_be.repositories.ProgramEducationCourseRepository;
import com.example.course_mapping_be.repositories.UniversityRepository;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private ModelMapper modelMapper;

    private JsonWebTokenProvider tokenProvider;
    private final UniversityRepository universityRepository;

    private final DocumentService documentService;
    private final ProgramEducationCourseRepository programEducationCourseRepository;

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
                .sourceLinks(courseDto.getSourceLinks())
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
        if (courseDto.getSourceLinks() != null) {
            course.setSourceLinks(courseDto.getSourceLinks());
        }
        courseRepository.save(course);
        baseResponse.setData(modelMapper.map(course, CourseDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public Float compareTwoCourse(Long id1, Long id2) {
        Course course1 = courseRepository.findById(id1).orElseThrow(() -> new RuntimeException("Course is not found"));
        Course course2 = courseRepository.findById(id2).orElseThrow(() -> new RuntimeException("Course is not found"));
        BaseResponse<Float> baseResponse = documentService.compareTwoDocuments(course1.getName(), course2.getName());
        return baseResponse.getData();
    }

    public Pair<Course, Float> getMostSimilarCourse(Course course, List<Course> courseList) {
        Pair<Course, Float> result = null;
        Course mostSimilarCourse = null;
        float maxSimilarity = 0;
        for (Course c : courseList) {
            if (!Objects.equals(c.getName(), course.getName())) {
                float similarity = compareTwoCourse(course.getId(), c.getId());
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    mostSimilarCourse = c;
                }
            } else {
                return Pair.of(c, 100.0f);
            }
        }
        if (mostSimilarCourse != null && maxSimilarity > 70.0f) {
            result = Pair.of(mostSimilarCourse, maxSimilarity);
        }
        return result;
    }

    public BaseResponse<List<CourseDto>> getAllByUniversity(Long id, QueryParams params) {
        University university = universityRepository.findById(id).orElseThrow(() -> new RuntimeException("University is not found"));
        Page<Course> courses = courseRepository.findByUniversityId(id, PageRequest.of(params.getPage(), params.getSize()));
        List<CourseDto> courseDtos = courses.stream().map(course -> modelMapper.map(course, CourseDto.class)).toList();
        BaseResponse<List<CourseDto>> baseResponse = new BaseResponse<>();
        baseResponse.setData(courseDtos);
        baseResponse.updatePagination(params, courses.getTotalElements());
        baseResponse.success();
        return baseResponse;

    }

    public BaseResponse<List<CourseDto>> search(SearchCourseDto searchCourseDto, QueryParams params) {
        BaseResponse<List<CourseDto>> baseResponse = new BaseResponse<>();

        Page<Course> programEducations = courseRepository.search(searchCourseDto, PageRequest.of(params.getPage(), params.getSize()));
        List<CourseDto> courseDtos = programEducations.stream().map(course -> modelMapper.map(course, CourseDto.class)).toList();
        baseResponse.setData(courseDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducations.getTotalElements());
        return baseResponse;
    }
}
