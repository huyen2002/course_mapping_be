package com.example.course_mapping_be.services;


import com.example.course_mapping_be.dtos.AddressDto;
import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.dtos.UniversityDto;
import com.example.course_mapping_be.models.Address;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.repositories.UniversityRepository;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UniversityService {
    private UniversityRepository universityRepository;

    private ModelMapper modelMapper;

    private JsonWebTokenProvider tokenProvider;

    public University createEmptyUniversity(User user) {
        University university = new University();
        university.setUser(user);
        return universityRepository.saveAndFlush(university);
    }

    public University createUniversity(University university) {
        university.setId(null);
        return universityRepository.save(university);
    }

    public BaseResponse<UniversityDto> update(UniversityDto universityDto, HttpServletRequest request) throws Exception {
        BaseResponse<UniversityDto> baseResponse = new BaseResponse<>();
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("Company is not found"));
        if (universityDto.getCode() != null) {
            university.setCode(universityDto.getCode());
        }
        if (universityDto.getIntroduction() != null) {
            university.setIntroduction(universityDto.getIntroduction());
        }
        if (universityDto.getFeature() != null) {
            university.setFeature(universityDto.getFeature());
        }


        Address address = university.getAddress();
        AddressDto addressDto = universityDto.getAddress();
        if (address == null && addressDto != null) {
            address = Address.builder().country(addressDto.getCountry())
                    .city(addressDto.getCity()).
                    district(addressDto.getDistrict()).detail(addressDto.getDetail()).build();

            university.setAddress(address);
        } else if (addressDto != null) {
            address.setCountry(addressDto.getCountry());
            address.setCity(addressDto.getCity());
            address.setDistrict(addressDto.getDistrict());
            address.setDetail(addressDto.getDetail());
        }
        university = universityRepository.save(university);
        baseResponse.setData(modelMapper.map(university, UniversityDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<List<UniversityDto>> getAll(QueryParams params) {
        Page<University> universities = universityRepository.findAll(PageRequest.of(params.getPage(), params.getSize()));
        List<UniversityDto> universityDtos = universities.stream().map(university -> modelMapper.map(university, UniversityDto.class)).toList();
        BaseResponse<List<UniversityDto>> baseResponse = new BaseResponse<>();
        baseResponse.setData(universityDtos);
        baseResponse.updatePagination(params, universities.getTotalElements());
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<UniversityDto> getById(Long id) throws Exception {
        University university = universityRepository.findById(id).orElseThrow(() -> new Exception("University with id is not found"));
        BaseResponse<UniversityDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(university, UniversityDto.class));
        baseResponse.success();
        return baseResponse;
    }
}
