package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.LevelEducationType;
import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.ProgramEducationDto;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.models.Major;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.repositories.MajorRepository;
import com.example.course_mapping_be.repositories.ProgramEducationRepository;
import com.example.course_mapping_be.repositories.UniversityRepository;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class ProgramEducationService {

    private JsonWebTokenProvider tokenProvider;

    private UniversityRepository universityRepository;
    private MajorRepository majorRepository;
    private ProgramEducationRepository programEducationRepository;
    private ModelMapper modelMapper;

    public BaseResponse<ProgramEducationDto> create(ProgramEducationDto programEducationDto, HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        Major major = majorRepository.findById(programEducationDto.getMajorId()).orElseThrow(() -> new Exception("Major is not found"));
        ProgramEducation programEducation = ProgramEducation.builder().name(programEducationDto.getName())
                .language(programEducationDto.getLanguage())
                .level_of_education(LevelEducationType.valueOf(programEducationDto.getLevel_of_education()))
                .university(university)
                .major(major).introduction(programEducationDto.getIntroduction())
                .duration_year(programEducationDto.getDuration_year())
                .num_credits(programEducationDto.getNum_credits())
                .outline(programEducationDto.getOutline())
                .start_year(programEducationDto.getStart_year())
                .end_year(programEducationDto.getEnd_year())
                .build();

        programEducationRepository.save(programEducation);
        BaseResponse<ProgramEducationDto> baseResponse = new BaseResponse<>();

        baseResponse.setData((modelMapper.map(programEducation, ProgramEducationDto.class)));
        baseResponse.success();
        return baseResponse;

    }

    public BaseResponse<List<ProgramEducationDto>> getAllByUniversityId(Long id, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationDto>> baseResponse = new BaseResponse<>();
        if (universityRepository.findById(id).isEmpty()) {
            throw new Exception("University is not found");
        }
        Page<ProgramEducation> programEducations = programEducationRepository.findAllByUniversityId(id, PageRequest.of(params.getPage(), params.getSize()));
        List<ProgramEducationDto> programEducationDtos = programEducations.map(programEducation -> modelMapper.map(programEducation, ProgramEducationDto.class)).getContent();
        baseResponse.setData(programEducationDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducations.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<List<ProgramEducationDto>> getAllByMajorId(Long id, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationDto>> baseResponse = new BaseResponse<>();
        if (majorRepository.findById(id).isEmpty()) {
            throw new Exception("Major with id is not found");
        }
        Page<ProgramEducation> programEducations = programEducationRepository.findAllByMajorId(id, PageRequest.of(params.getPage(), params.getSize()));
        List<ProgramEducationDto> programEducationDtos = programEducations.map(programEducation -> modelMapper.map(programEducation, ProgramEducationDto.class)).getContent();
        baseResponse.setData(programEducationDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducations.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<ProgramEducationDto> update(Long id, ProgramEducationDto programEducationDto, HttpServletRequest request) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(id).orElseThrow(() -> new Exception("Program education is not found"));
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        if (!programEducation.getUniversity().getId().equals(university.getId())) {
            throw new Exception("You are not authorized to update this program education");
        }
        if (programEducationDto.getName() != null) {
            programEducation.setName(programEducationDto.getName());
        }
        if (programEducationDto.getLanguage() != null) {
            programEducation.setLanguage(programEducationDto.getLanguage());
        }
        if (programEducationDto.getIntroduction() != null) {
            programEducation.setIntroduction(programEducationDto.getIntroduction());
        }
        if (programEducationDto.getDuration_year() != null) {
            programEducation.setDuration_year(programEducationDto.getDuration_year());
        }
        if (programEducationDto.getLevel_of_education() != null) {
            programEducation.setLevel_of_education(LevelEducationType.valueOf(programEducationDto.getLevel_of_education()));
        }
        if (programEducationDto.getNum_credits() != null) {
            programEducation.setNum_credits(programEducationDto.getNum_credits());
        }
        if (programEducationDto.getOutline() != null) {
            programEducation.setOutline(programEducationDto.getOutline());
        }
        if (programEducationDto.getStart_year() != null) {
            programEducation.setStart_year(programEducationDto.getStart_year());
        }
        if (programEducationDto.getEnd_year() != null) {
            programEducation.setEnd_year(programEducationDto.getEnd_year());
        }
        if (programEducationDto.getMajorId() != null) {
            Major major = majorRepository.findById(programEducationDto.getMajorId()).orElseThrow(() -> new Exception("Major is not found"));
            programEducation.setMajor(major);
        }
        programEducationRepository.save(programEducation);
        BaseResponse<ProgramEducationDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(programEducation, ProgramEducationDto.class));
        baseResponse.success();
        return baseResponse;

    }
}
