package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.DataStatus;
import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.ComparableProgramEducationDto;
import com.example.course_mapping_be.dtos.ComparedCourseDto;
import com.example.course_mapping_be.models.ComparableProgramEducation;
import com.example.course_mapping_be.repositories.ComparableProgramEducationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ComparableProgramEducationService {
    private ProgramEducationCourseService programEducationCourseService;
    private final ComparableProgramEducationRepository comparableProgramEducationRepository;
    private ModelMapper modelMapper;

    public BaseResponse<ComparableProgramEducationDto> create(ComparableProgramEducationDto comparableProgramEducationDto) throws JsonProcessingException {
        BaseResponse<ComparableProgramEducationDto> baseResponse = new BaseResponse<>();
        List<ComparedCourseDto> comparedCourseDtos = programEducationCourseService.compareCoursesOfProgramEducations(comparableProgramEducationDto.getFirstProgramId(), comparableProgramEducationDto.getSecondProgramId()).getData();
        // convert list of comparedCourseDtos to json string
        ObjectMapper objectMapper = new ObjectMapper();
        String coursesMapping = objectMapper.writeValueAsString(comparedCourseDtos);

        ComparableProgramEducation comparableProgramEducation = ComparableProgramEducation.builder()
                .firstProgramId(comparableProgramEducationDto.getFirstProgramId())
                .secondProgramId(comparableProgramEducationDto.getSecondProgramId())
                .coursesMapping(coursesMapping).
                status(DataStatus.UPDATED).
                build();

        ComparableProgramEducation test = comparableProgramEducationRepository.findByFirstProgramIdAndSecondProgramId(comparableProgramEducationDto.getFirstProgramId(), comparableProgramEducationDto.getSecondProgramId()).orElse(null);

        if (test != null) {
            baseResponse.setData(modelMapper.map(test, ComparableProgramEducationDto.class));
            baseResponse.success();
            return baseResponse;
        }
        comparableProgramEducationRepository.save(comparableProgramEducation);
        baseResponse.setData(modelMapper.map(comparableProgramEducation, ComparableProgramEducationDto.class));
        baseResponse.success();
        return baseResponse;

    }

    public BaseResponse<ComparableProgramEducationDto> update(ComparableProgramEducationDto comparableProgramEducationDto) throws Exception {
        BaseResponse<ComparableProgramEducationDto> baseResponse = new BaseResponse<>();
        ComparableProgramEducation comparableProgramEducation = comparableProgramEducationRepository.findByFirstProgramIdAndSecondProgramId(comparableProgramEducationDto.getFirstProgramId(), comparableProgramEducationDto.getSecondProgramId()).orElseThrow(() -> new Exception("Comparable program education is not found"));

        List<ComparedCourseDto> comparedCourseDtos = programEducationCourseService.compareCoursesOfProgramEducations(comparableProgramEducationDto.getFirstProgramId(), comparableProgramEducationDto.getSecondProgramId()).getData();
        String coursesMapping = comparedCourseDtos.stream().map(ComparedCourseDto::toString).reduce("", (a, b) -> a + b);
        comparableProgramEducation.setCoursesMapping(coursesMapping);
        comparableProgramEducationRepository.save(comparableProgramEducation);

        baseResponse.setData(modelMapper.map(comparableProgramEducation, ComparableProgramEducationDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<ComparableProgramEducationDto> getComparableProgramEducation(Long firstProgramId, Long secondProgramId) throws Exception {
        BaseResponse<ComparableProgramEducationDto> baseResponse = new BaseResponse<>();
        ComparableProgramEducation comparableProgramEducation = comparableProgramEducationRepository.findByFirstProgramIdAndSecondProgramId(firstProgramId, secondProgramId).orElse(null);
        if (comparableProgramEducation == null) {
            ComparableProgramEducationDto createdDto = this.create(new ComparableProgramEducationDto(firstProgramId, secondProgramId)).getData();
            baseResponse.setData(createdDto);
            baseResponse.success();
            return baseResponse;
        } else if (comparableProgramEducation.getStatus() == DataStatus.NEEDS_UPDATE) {
            ComparableProgramEducationDto updatedDto = this.update(new ComparableProgramEducationDto(firstProgramId, secondProgramId)).getData();
            baseResponse.setData(updatedDto);
            baseResponse.success();
            return baseResponse;

        }
        baseResponse.setData(modelMapper.map(comparableProgramEducation, ComparableProgramEducationDto.class));
        baseResponse.success();
        return baseResponse;
    }
}
