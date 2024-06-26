package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.RoleType;
import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.*;
import com.example.course_mapping_be.repositories.*;
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
    private UserRepository userRepository;

    public BaseResponse<CourseDto> create(CourseDto courseDto, HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User is not found"));
        University university = null;

        if (user.getRole() == RoleType.ADMIN) {
            university = universityRepository.findById(courseDto.getUniversityId()).orElseThrow(() -> new Exception("University is not found"));
        } else if (user.getRole() == RoleType.UNIVERSITY) {
            university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        }

        BaseResponse<CourseDto> baseResponse = new BaseResponse<>();
        if (courseRepository.existsByCode(courseDto.getCode())) {
            baseResponse.setStatus(400);
            baseResponse.setMessage("Mã khóa học đã tồn tại");
            return baseResponse;
        }
        String vectorOutline = null;
        if (courseDto.getOutline() != null) {
            String outline = readFileService.readData(courseDto.getOutline());
            vectorOutline = documentService.convertDocumentToVector(outline).getData();
        }

        String vectorName = documentService.convertDocumentToVectorDbow(courseDto.getName()).getData();
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

        Course course = courseRepository.findById(id).orElseThrow(() -> new Exception("Course is not found"));

        BaseResponse<CourseDto> baseResponse = new BaseResponse<>();
        if (courseDto.getName() != null) {
            course.setName(courseDto.getName());
            course.setVectorName(documentService.convertDocumentToVectorDbow(courseDto.getName()).getData());
        }
        if (courseDto.getCode() != null) {
            if (courseRepository.existsByCode(courseDto.getCode())) {
                baseResponse.setStatus(400);
                baseResponse.setMessage("Mã khóa học đã tồn tại");
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
        BaseResponse<Float> baseResponse = new BaseResponse<>();
        Float nameSimilarity = documentService.compareTwoVectorsFromString(course1.getVectorName(), course2.getVectorName());
        if (course1.getVectorOutline() == null || course2.getVectorOutline() == null) {

            return nameSimilarity * 100.0f;

        }
        Float outlineSimilarity = documentService.compareTwoVectorsFromString(course1.getVectorOutline(), course2.getVectorOutline());
        Float similarity = (nameSimilarity * 0.6f + outlineSimilarity * 0.4f) * 100.0f;
        baseResponse.setData(similarity);
        return baseResponse.getData();
    }

    public Pair<Course, Float> getMostSimilarCourse(Course course, List<Course> courseList) {
        Pair<Course, Float> result = null;
        Course mostSimilarCourse = null;
        float maxSimilarity = 0;
        for (Course c : courseList) {
            if (!Objects.equals(c.getId(), course.getId())) {
                float similarity = compareTwoCourse(course.getId(), c.getId());
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    mostSimilarCourse = c;
                }
            } else {
                return Pair.of(c, 100.0f);
            }
        }
        if (mostSimilarCourse != null && maxSimilarity > 50.0f) {
            maxSimilarity = Math.round(maxSimilarity * 100.0f) / 100.0f;
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


}
