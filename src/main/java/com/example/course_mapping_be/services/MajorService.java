package com.example.course_mapping_be.services;


import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.MajorDto;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.models.Major;
import com.example.course_mapping_be.repositories.MajorRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MajorService {

    private MajorRepository majorRepository;

    private ModelMapper modelMapper;

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

    public BaseResponse<List<MajorDto>> getAll(QueryParams params) {
        BaseResponse<List<MajorDto>> baseResponse = new BaseResponse<>();
        Page<Major> majors = majorRepository.findAll(PageRequest.of(params.getPage(), params.getSize()));
        List<MajorDto> majorDtos = majors.stream().map(major -> modelMapper.map(major, MajorDto.class)).toList();
        baseResponse.setData(majorDtos);
        baseResponse.updatePagination(params, majors.getTotalElements());
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

    public void delete(Long id) {
    }

    public Boolean deleteById(Long id) throws Exception {
        majorRepository.findById(id).orElseThrow(() -> new Exception("Major with id is not found"));
        majorRepository.deleteById(id);
        return true;
    }
}
