package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.DataStatus;
import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.ComparableProgramEducationDto;
import com.example.course_mapping_be.dtos.ComparedCourseDto;
import com.example.course_mapping_be.dtos.CourseDto;
import com.example.course_mapping_be.models.ComparableProgramEducation;
import com.example.course_mapping_be.models.ComparedCourseElement;
import com.example.course_mapping_be.models.Course;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.repositories.ComparableProgramEducationRepository;
import com.example.course_mapping_be.repositories.CourseRepository;
import com.example.course_mapping_be.repositories.ProgramEducationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ComparableProgramEducationService {
    private ProgramEducationCourseService programEducationCourseService;
    private final ComparableProgramEducationRepository comparableProgramEducationRepository;
    private ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private DocumentService documentService;
    private ProgramEducationRepository programEducationRepository;

    public String convertCompareElementToDto(String comparedElements) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //convert json string to list of comparedCourseElements
        List<ComparedCourseElement> comparedCourseElements = objectMapper.readValue(comparedElements, objectMapper.getTypeFactory().constructCollectionType(List.class, ComparedCourseElement.class));
        //convert list of comparedCourseElements to list of comparedCourseDtos
        List<ComparedCourseDto> comparedCourseDtos = new ArrayList<>();
        comparedCourseElements.forEach(element -> {
            Course firstCourse = null;
            Course secondCourse = null;
            if (element.getFirstCourseId() != null) {
                firstCourse = courseRepository.findById(element.getFirstCourseId()).orElse(null);

            }
            if (element.getSecondCourseId() != null) {
                secondCourse = courseRepository.findById(element.getSecondCourseId()).orElse(null);
            }
            ComparedCourseDto comparedCourseDto = null;
            if (firstCourse != null && secondCourse != null) {
                comparedCourseDto = new ComparedCourseDto(
                        modelMapper.map(firstCourse, CourseDto.class),
                        modelMapper.map(secondCourse, CourseDto.class),
                        element.getSimilarity()
                );
            } else if (firstCourse == null) {
                comparedCourseDto = new ComparedCourseDto(
                        null,
                        modelMapper.map(secondCourse, CourseDto.class),
                        element.getSimilarity()
                );
            } else {
                comparedCourseDto = new ComparedCourseDto(
                        modelMapper.map(firstCourse, CourseDto.class),
                        null,
                        element.getSimilarity()
                );

            }

            comparedCourseDtos.add(comparedCourseDto);
        });
        // convert comparedCourseDtos to string
        String coursesMapping = objectMapper.writeValueAsString(comparedCourseDtos);
        return coursesMapping;
    }

    public BaseResponse<ComparableProgramEducationDto> create(ComparableProgramEducationDto comparableProgramEducationDto) throws JsonProcessingException, Exception {
        BaseResponse<ComparableProgramEducationDto> baseResponse = new BaseResponse<>();
        ProgramEducation firstProgram = programEducationRepository.findById(comparableProgramEducationDto.getFirstProgramId()).orElse(null);
        ProgramEducation secondProgram = programEducationRepository.findById(comparableProgramEducationDto.getSecondProgramId()).orElse(null);
        if (firstProgram == null || secondProgram == null) {
            throw new Exception("Program education is not found");
        }
        List<ComparedCourseElement> comparedCourseDtos = programEducationCourseService.compareCoursesOfProgramEducations(firstProgram.getId(), secondProgram.getId()).getData();
        // convert list of comparedCourseDtos to json string
        ObjectMapper objectMapper = new ObjectMapper();
        String coursesMapping = objectMapper.writeValueAsString(comparedCourseDtos);

        Float nameSimilarity = documentService.compareTwoVectorsFromString(firstProgram.getVectorName(), secondProgram.getVectorName());
        Float outlineSimilarity = 0.0f;
        if (firstProgram.getVectorOutline() != null && secondProgram.getVectorOutline() != null) {
            outlineSimilarity = documentService.compareTwoVectorsFromString(firstProgram.getVectorOutline(), secondProgram.getVectorOutline());

        }

        Float introductionSimilarity = 0.0f;
        if (firstProgram.getIntroduction() != null && secondProgram.getIntroduction() != null) {
            introductionSimilarity = documentService.compareTwoDocuments(firstProgram.getIntroduction(), secondProgram.getIntroduction()).getData();

        }
        nameSimilarity = Math.round(nameSimilarity * 100 * 100.0) / 100.0f;
        outlineSimilarity = Math.round(outlineSimilarity * 100 * 100.0) / 100.0f;

        ComparableProgramEducation comparableProgramEducation = ComparableProgramEducation.builder()
                .firstProgramId(comparableProgramEducationDto.getFirstProgramId())
                .secondProgramId(comparableProgramEducationDto.getSecondProgramId())
                .nameSimilarity(nameSimilarity)
                .introductionSimilarity(introductionSimilarity)
                .outlineSimilarity(outlineSimilarity)
                .coursesMapping(coursesMapping).
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


    public BaseResponse<ComparableProgramEducationDto> getComparableProgramEducation(Long firstProgramId, Long secondProgramId) throws Exception {
        BaseResponse<ComparableProgramEducationDto> baseResponse = new BaseResponse<>();
        ComparableProgramEducation comparableProgramEducation = comparableProgramEducationRepository.findByFirstProgramIdAndSecondProgramId(firstProgramId, secondProgramId).orElse(null);
        if (comparableProgramEducation == null) {
            ComparableProgramEducationDto createdDto = this.create(new ComparableProgramEducationDto(firstProgramId, secondProgramId)).getData();
            createdDto.setCoursesMapping(convertCompareElementToDto(createdDto.getCoursesMapping()));
            baseResponse.setData(createdDto);
            baseResponse.success();
            return baseResponse;
        }
        //convert json string to list of comparedCourseElements

        String coursesMapping = convertCompareElementToDto(comparableProgramEducation.getCoursesMapping());
        comparableProgramEducation.setCoursesMapping(coursesMapping);

        baseResponse.setData(modelMapper.map(comparableProgramEducation, ComparableProgramEducationDto.class));
        baseResponse.success();
        return baseResponse;
    }


}
