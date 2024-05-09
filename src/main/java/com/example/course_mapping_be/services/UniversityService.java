package com.example.course_mapping_be.services;


import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.*;
import com.example.course_mapping_be.repositories.*;
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
public class UniversityService {
    private UniversityRepository universityRepository;

    private ModelMapper modelMapper;

    private JsonWebTokenProvider tokenProvider;

    private AddressService addressService;
    private final AddressRepository addressRepository;
    private final ProgramEducationRepository programEducationRepository;
    private final CourseRepository courseRepository;
    private final ProgramEducationCourseRepository programEducationCourseRepository;

    public University createEmptyUniversity(User user) {
        University university = new University();
        university.setUser(user);
        return universityRepository.saveAndFlush(university);
    }

    public University createUniversity(University university) {
        university.setId(null);
        return universityRepository.save(university);
    }

    public BaseResponse<UniversityDto> create(UniversityDto universityDto) {
        Address address = null;
        if (universityDto.getAddress() != null) {
            AddressDto addressDto = universityDto.getAddress();
            address = Address.builder().country(addressDto.getCountry())
                    .city(addressDto.getCity()).district(addressDto.getDistrict())
                    .detail(addressDto.getDetail())
                    .build();

        }

        University university = University.builder()
                .name(universityDto.getName()).code(universityDto.getCode()).user(null).
                introduction(universityDto.getIntroduction()).address(address)
                .feature(universityDto.getFeature()).build();

        BaseResponse<UniversityDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(universityRepository.save(university), UniversityDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<UniversityDto> updateByUniversityId(Long universityId, UniversityDto universityDto) throws Exception {
        BaseResponse<UniversityDto> baseResponse = new BaseResponse<>();
        University university = universityRepository.findById(universityId).orElseThrow(() -> new Exception("University is not found"));

        if (universityDto.getName() != null) {
            if (universityRepository.findByName(universityDto.getName()).isPresent()) {
                throw new Exception("University with name is existed");
            }
            university.setName(universityDto.getName());
        }
        if (universityDto.getCode() != null) {
            if (universityRepository.findByCode(universityDto.getCode()).isPresent()) {
                throw new Exception("University with code is existed");
            }
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
            addressService.update(address.getId(), addressDto);
        }
        university = universityRepository.save(university);
        baseResponse.setData(modelMapper.map(university, UniversityDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<UniversityDto> update(UniversityDto universityDto, HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);

        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        BaseResponse<UniversityDto> baseResponse = this.updateByUniversityId(university.getId(), universityDto);
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

    public BaseResponse<List<UniversityDto>> search(FilterUniversityParams filterParams, QueryParams params) {
        Page<University> universities = universityRepository.filterUniversities(filterParams, PageRequest.of(params.getPage(), params.getSize()));
        List<UniversityDto> universityDtos = universities.stream().map(university -> modelMapper.map(university, UniversityDto.class)).toList();
        BaseResponse<List<UniversityDto>> baseResponse = new BaseResponse<>();
        baseResponse.setData(universityDtos);
        baseResponse.updatePagination(params, universities.getTotalElements());
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<List<UniversityDto>> getList() {
        List<University> universities = universityRepository.findAll();
        List<UniversityDto> universityDtos = universities.stream().map(university -> modelMapper.map(university, UniversityDto.class)).toList();
        BaseResponse<List<UniversityDto>> baseResponse = new BaseResponse<>();
        baseResponse.setData(universityDtos);
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

    public BaseResponse<UniversityDto> getByUser(HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElse(null);
        BaseResponse<UniversityDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(university, UniversityDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<Boolean> deleteById(Long id) throws Exception {
        BaseResponse<Boolean> baseResponse = new BaseResponse<>();
        University university = universityRepository.findById(id).orElseThrow(() -> new Exception("University is not found"));
        List<ProgramEducation> programEducations = programEducationRepository.findByUniversityId(id);
        List<Course> courses = courseRepository.findAllByUniversityId(id);
        programEducations.forEach((program) -> {
            programEducationCourseRepository.deleteByProgramId(program.getId());

        });
        courses.forEach((course) -> {
            courseRepository.deleteById(course.getId());
        });
        programEducationRepository.deleteByUniversityId(id);
        courseRepository.deleteByUniversityId(id);
        if (university.getAddress() != null) {
            addressRepository.deleteById(university.getAddress().getId());

        }
        universityRepository.deleteById(id);
        baseResponse.success();
        baseResponse.setData(true);
        return baseResponse;
    }


    public BaseResponse<UniversityDto> updateEnable(Long id, UniversityDto universityDto) throws Exception {
        University university = universityRepository.findById(id).orElseThrow(() -> new Exception("University is not found"));
        Boolean enabled = universityDto.getEnabled();
        programEducationRepository.updateEnabledByUniversityId(id, enabled);

        university.setEnabled(enabled);
        university = universityRepository.save(university);
        BaseResponse<UniversityDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(university, UniversityDto.class));
        baseResponse.success();
        return baseResponse;
    }
}
