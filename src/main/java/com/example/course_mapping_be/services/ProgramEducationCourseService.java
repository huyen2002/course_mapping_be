package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.ComparedCourseElement;
import com.example.course_mapping_be.models.Course;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.ProgramEducationCourse;
import com.example.course_mapping_be.repositories.ComparableProgramEducationRepository;
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
    private final ComparableProgramEducationRepository comparableProgramEducationRepository;

    public void deleteComparableProgram(Long programCourseId) {
        ProgramEducationCourse programEducationCourse = programEducationCourseRepository.findById(programCourseId).orElse(null);
        if (programEducationCourse == null) {
            return;
        }
        comparableProgramEducationRepository.deleteByProgramId(programEducationCourse.getProgramEducation().getId());
    }

    public ProgramEducationCourseDto convertToDto(ProgramEducationCourse programEducationCourse) {
        ProgramEducationCourseDto programEducationCourseDto = modelMapper.map(programEducationCourse, ProgramEducationCourseDto.class);
        programEducationCourseDto.setProgramEducationId(programEducationCourse.getProgramEducation().getId());
        programEducationCourseDto.setCourseId(programEducationCourse.getCourse().getId());
        programEducationCourseDto.setCourse(modelMapper.map(programEducationCourse.getCourse(), CourseDto.class));
        return programEducationCourseDto;
    }

    public BaseResponse<ProgramEducationCourseDto> create(ProgramEducationCourseDto programEducationCourseDto) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(programEducationCourseDto.getProgramEducationId()).orElseThrow(() -> new Exception("Program education is not found"));
        Course course = courseRepository.findById(programEducationCourseDto.getCourseId()).orElseThrow(() -> new Exception("Course is not found"));
        ProgramEducationCourse findProgramEducationCourse = programEducationCourseRepository.findByProgramEducationIdAndCourseId(programEducation.getId(), course.getId()).orElse(null);
        if (findProgramEducationCourse != null) {
            throw new Exception("Program education course is already exist");
        }
        ProgramEducationCourse programEducationCourse = ProgramEducationCourse.builder()
                .programEducation(programEducation).course(course).
                compulsory(programEducationCourseDto.getCompulsory())
                .numCredits(programEducationCourseDto.getNumCredits()).build();

        programEducationCourseRepository.save(programEducationCourse);
        deleteComparableProgram(programEducationCourse.getId());
        BaseResponse<ProgramEducationCourseDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(convertToDto(programEducationCourse));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<List<ProgramEducationCourseDto>> search(SearchCourseDto searchCourseDto, QueryParams params) {
        Page<ProgramEducationCourse> programEducationCourses = programEducationCourseRepository.searchCourses(searchCourseDto, PageRequest.of(params.getPage(), params.getSize()));
        List<ProgramEducationCourseDto> programEducationCourseDtos = programEducationCourses.stream().map(this::convertToDto).toList();
        BaseResponse<List<ProgramEducationCourseDto>> baseResponse = new BaseResponse<>();
        baseResponse.setData(programEducationCourseDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducationCourses.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<List<ComparedCourseElement>> compareCoursesOfProgramEducations(Long programId1, Long programId2) {
        List<ProgramEducationCourse> programCourseList1 = programEducationCourseRepository.findAllByProgramEducationId(programId1);
        List<ProgramEducationCourse> programCourseList2 = programEducationCourseRepository.findAllByProgramEducationId(programId2);
        List<Course> courseList1 = programCourseList1.stream().map(ProgramEducationCourse::getCourse).toList();
        List<Course> courseList2 = programCourseList2.stream().map(ProgramEducationCourse::getCourse).toList();
        List<ComparedCourseElement> comparedCourseElements = new ArrayList<>();
        List<Course> copyCourseList2 = new ArrayList<>(courseList2);
        for (Course course : courseList1) {
            Pair<Course, Float> mostSimilarCourse = courseService.getMostSimilarCourse(course, courseList2);
            if (mostSimilarCourse != null) {
                comparedCourseElements.add(
                        new ComparedCourseElement(course.getId(), mostSimilarCourse.getFirst().getId(), mostSimilarCourse.getSecond()));

                copyCourseList2.remove(mostSimilarCourse.getFirst());
            } else {
                comparedCourseElements.add(
                        new ComparedCourseElement(course.getId(), null, 0f));
            }

        }

        if (!copyCourseList2.isEmpty()) {
            for (Course course : copyCourseList2) {
                comparedCourseElements.add(
                        new ComparedCourseElement(null, course.getId(), 0f)
                );
            }
        }
        BaseResponse<List<ComparedCourseElement>> baseResponse = new BaseResponse<>();
        baseResponse.setData(comparedCourseElements);
        baseResponse.success();
        return baseResponse;

    }


    public Boolean deleteById(Long id) {
        deleteComparableProgram(id);
        programEducationCourseRepository.deleteById(id);

        return true;
    }

    public BaseResponse<ProgramEducationCourseDto> update(Long id, ProgramEducationCourseDto programEducationCourseDto) throws Exception {
        ProgramEducationCourse programEducationCourse = programEducationCourseRepository.findById(id).orElseThrow(() -> new Exception("Program education course is not found"));
        if (programEducationCourseDto.getProgramEducationId() != null) {
            ProgramEducation programEducation = programEducationRepository.findById(programEducationCourseDto.getProgramEducationId()).orElseThrow(() -> new Exception("Program education is not found"));
            programEducationCourse.setProgramEducation(programEducation);
        }

        if (programEducationCourseDto.getCompulsory() != null) {
            programEducationCourse.setCompulsory(programEducationCourseDto.getCompulsory());
        }
        if (programEducationCourseDto.getNumCredits() != null) {
            programEducationCourse.setNumCredits(programEducationCourseDto.getNumCredits());
        }
        programEducationCourseRepository.save(programEducationCourse);
        BaseResponse<ProgramEducationCourseDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(convertToDto(programEducationCourse));
        baseResponse.success();
        return baseResponse;


    }
}
