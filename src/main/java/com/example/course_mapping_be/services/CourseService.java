package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.Course;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.ProgramEducationCourse;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.repositories.ComparableProgramEducationRepository;
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
    private final ComparableProgramEducationRepository comparableProgramEducationRepository;
    private ReadFileService readFileService;

    public BaseResponse<CourseDto> create(CourseDto courseDto, HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));

        BaseResponse<CourseDto> baseResponse = new BaseResponse<>();
        if (courseRepository.existsByCode(courseDto.getCode())) {
            throw new Exception("Code is already used");
        }
        String vectorOutline = null;
        if (courseDto.getOutline() != null) {
            String outline = readFileService.readData(courseDto.getOutline());
            vectorOutline = documentService.convertDocumentToVector(outline).getData();
        }

        String vectorName = documentService.convertDocumentToVector(courseDto.getName()).getData();
        Course course = Course.builder()
                .name(courseDto.getName())
                .code(courseDto.getCode())
                .language(courseDto.getLanguage())
                .outline(courseDto.getOutline())
                .university(university)
                .vectorName(vectorName)
                .vectorOutline(vectorOutline)
                .build();

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
            course.setVectorName(documentService.convertDocumentToVector(courseDto.getName()).getData());
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
            String outline = readFileService.readData(courseDto.getOutline());
            course.setVectorOutline(documentService.convertDocumentToVector(outline).getData());

        }

        if (courseDto.getName() != null || courseDto.getOutline() != null) {
            List<ProgramEducationCourse> programEducationCourses = programEducationCourseRepository.findAllByCourseId(course.getId());
            programEducationCourses.forEach(programEducationCourse -> {
                comparableProgramEducationRepository.deleteByProgramId(programEducationCourse.getProgramEducation().getId());
            });
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


    public BaseResponse<List<CourseDto>> search(SearchCourseDto searchCourseDto, QueryParams params) {
        BaseResponse<List<CourseDto>> baseResponse = new BaseResponse<>();

        Page<Course> programEducations = courseRepository.search(searchCourseDto, PageRequest.of(params.getPage(), params.getSize()));
        List<CourseDto> courseDtos = programEducations.stream().map(course -> modelMapper.map(course, CourseDto.class)).toList();
        baseResponse.setData(courseDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducations.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<List<CourseDto>> getListByUniversity(Long id) throws Exception {
        BaseResponse<List<CourseDto>> baseResponse = new BaseResponse<>();
        List<Course> courses = courseRepository.findAllByUniversityId(id);
        List<CourseDto> courseDtos = courses.stream().map(course -> modelMapper.map(course, CourseDto.class)).toList();
        baseResponse.setData(courseDtos);
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<Boolean> deleteById(Long id) {
        List<ProgramEducationCourse> programEducationCourses = programEducationCourseRepository.findAllByCourseId(id);
        programEducationCourses.forEach(programEducationCourse -> {
            comparableProgramEducationRepository.deleteByProgramId(programEducationCourse.getProgramEducation().getId());
        });
        programEducationCourseRepository.deleteByCourseId(id);
        courseRepository.deleteById(id);
        BaseResponse<Boolean> baseResponse = new BaseResponse<>();
        baseResponse.setData(true);
        baseResponse.success();
        return baseResponse;

    }

    public BaseResponse<Boolean> existedByCode(String code) {
        BaseResponse<Boolean> baseResponse = new BaseResponse<>();
        baseResponse.setData(courseRepository.existsByCode(code));
        baseResponse.success();
        return baseResponse;
    }

}
