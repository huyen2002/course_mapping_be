package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.CourseDto;
import com.example.course_mapping_be.dtos.ProgramEducationCourseDto;
import com.example.course_mapping_be.dtos.QueryParams;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProgramEducationCourseService {

    private ProgramEducationRepository programEducationRepository;
    private CourseRepository courseRepository;
    private ModelMapper modelMapper;
    private final ProgramEducationCourseRepository programEducationCourseRepository;

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

    public Boolean deleteById(Long id) {
        programEducationCourseRepository.deleteById(id);
        return true;
    }
}
