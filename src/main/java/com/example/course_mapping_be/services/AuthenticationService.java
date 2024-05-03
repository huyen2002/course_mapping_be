package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.RoleType;
import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.LoginRequestDto;
import com.example.course_mapping_be.dtos.UserCreateDto;
import com.example.course_mapping_be.dtos.UserDto;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.security.CustomUserDetails;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private UserService userService;

    private ModelMapper modelMapper;

    private UniversityService universityService;

    private AuthenticationManager authenticationManager;
    private JsonWebTokenProvider tokenProvider;

    public BaseResponse<UserDto> register(UserCreateDto userCreateDto) throws Exception {
        BaseResponse<UserDto> baseResponse = new BaseResponse<>();
        User user = userService.createUser(userCreateDto);

        if (user.getRole() == RoleType.UNIVERSITY) {
            universityService.createEmptyUniversity(user);
            universityService.updateByUniversityId(user.getId(), userCreateDto.getUniversity());
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

    public BaseResponse<UserDto> me(HttpServletRequest request) throws Exception {
        Long id = tokenProvider.getUserIdFromRequest(request);
        return userService.getUserById(id);
    }
}
