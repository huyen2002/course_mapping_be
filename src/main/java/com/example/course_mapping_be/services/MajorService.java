package com.example.course_mapping_be.services;


import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.Major;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.repositories.MajorRepository;
import com.example.course_mapping_be.repositories.ProgramEducationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MajorService {

    private MajorRepository majorRepository;

    private ModelMapper modelMapper;

    private ProgramEducationRepository programEducationRepository;
    private ProgramEducationService programEducationService;

    public MajorDto convertToDto(Major major) {
        MajorDto majorDto = modelMapper.map(major, MajorDto.class);
        majorDto.setNumberOfProgramEducations(major.getProgramEducations().size());
        return majorDto;
    }

    public BaseResponse<MajorDto> create(MajorDto majorDto) throws Exception {
        BaseResponse<MajorDto> baseResponse = new BaseResponse<>();
        if (majorRepository.findByCode(majorDto.getCode()).isPresent()) {
            throw new Exception("Major with code is already exist");
        }
        Major major = new Major(majorDto.getCode(), majorDto.getName());

        majorRepository.save(major);
        baseResponse.setData(modelMapper.map(major, MajorDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<List<MajorDto>> getAll() {
        BaseResponse<List<MajorDto>> baseResponse = new BaseResponse<>();
        List<Major> majors = majorRepository.findAllEnabledMajors();
        List<MajorDto> majorDtos = majors.stream().map(this::convertToDto).toList();
        baseResponse.setData(majorDtos);
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<MajorDto> update(Long id, MajorDto majorDto) throws Exception {
        Major major = majorRepository.findById(id).orElseThrow(() -> new Exception("Major with id is not found"));
        if (majorDto.getName() != null) {
            major.setName(majorDto.getName());
        }
        if (majorDto.getCode() != null) {
            if (majorRepository.findByCode(majorDto.getCode()).isPresent())
                throw new Exception("Major with code is already exist");

            major.setCode(majorDto.getCode());
        }
        majorRepository.save(major);
        BaseResponse<MajorDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(major, MajorDto.class));
        baseResponse.success();
        return baseResponse;
    }


    public BaseResponse<Boolean> deleteById(Long id, HttpServletRequest request) throws Exception {
        majorRepository.findById(id).orElseThrow(() -> new Exception("Major with id is not found"));
        List<ProgramEducation> programEducations = programEducationRepository.getAllByMajorId(id);
        programEducations.forEach(programEducation -> {
            try {
                programEducationService.deleteById(programEducation.getId(), request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        majorRepository.deleteById(id);
        BaseResponse<Boolean> baseResponse = new BaseResponse<>();
        baseResponse.setData(true);
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<List<MajorDto>> searchMajors(SearchMajorDto searchMajorDto, QueryParams params) {
        BaseResponse<List<MajorDto>> baseResponse = new BaseResponse<>();
        Page<Major> majors = majorRepository.searchMajors(searchMajorDto, PageRequest.of(params.getPage(), params.getSize()));
        List<MajorDto> majorDtos = majors.map(this::convertToDto).getContent();
        baseResponse.setData(majorDtos);
        baseResponse.updatePagination(params, majors.getTotalElements());
        baseResponse.success();
        return baseResponse;
    }


    public BaseResponse<MajorDto> updateEnabled(Long id, MajorDto majorDto) throws Exception {
        Major major = majorRepository.findById(id).orElseThrow(() -> new Exception("Major is not found"));
        Boolean enabled = majorDto.getEnabled();
        programEducationRepository.updateEnabledByMajorId(id, enabled);

        major.setEnabled(enabled);
        major = majorRepository.save(major);
        BaseResponse<MajorDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(major, MajorDto.class));
        baseResponse.success();
        return baseResponse;
    }
}
