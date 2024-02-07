package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.RoleType;
import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.LoginRequestDto;
import com.example.course_mapping_be.dtos.UserCreateDto;
import com.example.course_mapping_be.dtos.UserDto;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.security.CustomUserDetails;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private final UserService userService;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final UniversityService universityService;

    private final AuthenticationManager authenticationManager;
    private final JsonWebTokenProvider tokenProvider;

    public AuthenticationService(UserService userService, ModelMapper modelMapper, UniversityService universityService, AuthenticationManager authenticationManager, JsonWebTokenProvider tokenProvider) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.universityService = universityService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public BaseResponse<UserDto> register(UserCreateDto userCreateDto) {
        BaseResponse<UserDto> baseResponse = new BaseResponse<>();
        User user = userService.createUser(userCreateDto);

        if (user.getRole() == RoleType.UNIVERSITY) {
            universityService.createEmptyUniversity(user);
        }

        baseResponse.setData(modelMapper.map(user, UserDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public Map<String, Object> login(LoginRequestDto loginRequestDto) {
        User user = userService.getUserByEmail(loginRequestDto.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  Return JWT
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return Map.of(
                "tokenType", "Bearer",
                "accessToken", jwt,
                "user", modelMapper.map(user, UserDto.class)
        );
//        return new JsonWebTokenModel("Bearer", jwt);
    }
}
