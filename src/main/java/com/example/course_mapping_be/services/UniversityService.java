package com.example.course_mapping_be.services;


import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.UniversityDto;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.repositories.UniversityRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;

@Service
public class UniversityService {

    @Autowired
    private final UniversityRepository universityRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public UniversityService(UniversityRepository universityRepository, ModelMapper modelMapper) {
        this.universityRepository = universityRepository;
        this.modelMapper = modelMapper;
    }

    public University createEmptyUniversity(User user) {
        University university = new University();
        university.setUser(user);
        return universityRepository.saveAndFlush(university);
    }

    public University createUniversity(University university) {
        university.setId(null);
        return universityRepository.save(university);
    }

//    public BaseResponse<UniversityDto> update(UniversityDto universityDto, HttpServletRequest request) {
//
//    }
}
