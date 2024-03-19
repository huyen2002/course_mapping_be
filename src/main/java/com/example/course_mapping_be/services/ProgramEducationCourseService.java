package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.Course;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.ProgramEducationCourse;
import com.example.course_mapping_be.repositories.CourseRepository;
import com.example.course_mapping_be.repositories.ProgramEducationCourseRepository;
import com.example.course_mapping_be.repositories.ProgramEducationRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProgramEducationCourseService {

    private ProgramEducationRepository programEducationRepository;
    private CourseRepository courseRepository;
    private ModelMapper modelMapper;
    private final ProgramEducationCourseRepository programEducationCourseRepository;
    private CourseService courseService;

    public ProgramEducationCourseDto convertToDto(ProgramEducationCourse programEducationCourse) {
        ProgramEducationCourseDto programEducationCourseDto = modelMapper.map(programEducationCourse, ProgramEducationCourseDto.class);
        programEducationCourseDto.setProgram_education_id(programEducationCourse.getProgram_education().getId());
        programEducationCourseDto.setCourse_id(programEducationCourse.getCourse().getId());
        programEducationCourseDto.setCourse(modelMapper.map(programEducationCourse.getCourse(), CourseDto.class));
        return programEducationCourseDto;
    }

    public BaseResponse<ProgramEducationCourseDto> create(ProgramEducationCourseDto programEducationCourseDto) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(programEducationCourseDto.getProgram_education_id()).orElseThrow(() -> new Exception("Program education is not found"));
        Course course = courseRepository.findById(programEducationCourseDto.getCourse_id()).orElseThrow(() -> new Exception("Course is not found"));
        if (programEducationCourseRepository.findByProgramEducationIdAndCourseId(programEducation.getId(), course.getId()).isPresent()) {
            throw new Exception("Program education course is already exist");
        }
        ProgramEducationCourse programEducationCourse = ProgramEducationCourse.builder()
                .program_education(programEducation).course(course).
                compulsory(programEducationCourseDto.getCompulsory())
                .num_credits(programEducationCourseDto.getNum_credits()).build();

        programEducationCourseRepository.save(programEducationCourse);
        BaseResponse<ProgramEducationCourseDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(convertToDto(programEducationCourse));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<List<ProgramEducationCourseDto>> getAllCoursesByProgramEducationId(Long id, QueryParams params) {
        Page<ProgramEducationCourse> programEducationCourses = programEducationCourseRepository.findAllByProgramEducationId(id, PageRequest.of(params.getPage(), params.getSize()));
        List<ProgramEducationCourseDto> programEducationCourseDtos = programEducationCourses.stream().map(this::convertToDto).toList();
        BaseResponse<List<ProgramEducationCourseDto>> baseResponse = new BaseResponse<>();
        baseResponse.setData(programEducationCourseDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducationCourses.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<List<ComparedCourseDto>> compareCoursesOfProgramEducations(Long programId1, Long programId2) {
        List<ProgramEducationCourse> programCourseList1 = programEducationCourseRepository.findAllByProgramEducationId(programId1);
        List<ProgramEducationCourse> programCourseList2 = programEducationCourseRepository.findAllByProgramEducationId(programId2);
        List<Course> courseList1 = programCourseList1.stream().map(ProgramEducationCourse::getCourse).toList();
        List<Course> courseList2 = programCourseList2.stream().map(ProgramEducationCourse::getCourse).toList();
        List<ComparedCourseDto> comparedCourseDtos = new ArrayList<>();
        List<Course> copyCourseList2 = new ArrayList<>(courseList2);
        for (Course course : courseList1) {
            Pair<Course, Float> mostSimilarCourse = courseService.getMostSimilarCourse(course, courseList2);
            if (mostSimilarCourse != null) {
                comparedCourseDtos.add(ComparedCourseDto.builder()
                        .course1(modelMapper.map(course, CourseDto.class))
                        .course2(modelMapper.map(mostSimilarCourse.getFirst(), CourseDto.class))
                        .similarity(mostSimilarCourse.getSecond())
                        .build());
                copyCourseList2.remove(mostSimilarCourse.getFirst());
            } else {
                comparedCourseDtos.add(ComparedCourseDto.builder()
                        .course1(modelMapper.map(course, CourseDto.class))
                        .course2(null)
                        .similarity(0f)
                        .build());
            }

        }

        if (!copyCourseList2.isEmpty()) {
            for (Course course : copyCourseList2) {
                comparedCourseDtos.add(ComparedCourseDto.builder()
                        .course1(null)
                        .course2(modelMapper.map(course, CourseDto.class))
                        .similarity(0f)
                        .build());
            }
        }
        BaseResponse<List<ComparedCourseDto>> baseResponse = new BaseResponse<>();
        baseResponse.setData(comparedCourseDtos);
        baseResponse.success();
        return baseResponse;

    }

    public Boolean deleteById(Long id) {
        programEducationCourseRepository.deleteById(id);
        return true;
    }
}
