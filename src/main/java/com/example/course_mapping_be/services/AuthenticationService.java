package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.RoleType;
import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.repositories.UniversityRepository;
import com.example.course_mapping_be.repositories.UserRepository;
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
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;

    public BaseResponse<UserDto> register(UserCreateDto userCreateDto) throws Exception {
        BaseResponse<UserDto> baseResponse = new BaseResponse<>();
        if (userRepository.findByEmail(userCreateDto.getEmail()).isPresent()) {
            baseResponse.setStatus(400);
            baseResponse.setMessage("Email đã tồn tại");
            return baseResponse;
        }
        if (userRepository.findByUsername(userCreateDto.getUsername()).isPresent()) {
            baseResponse.setStatus(400);
            baseResponse.setMessage("Username đã tồn tại");
            return baseResponse;
        }

        if (universityRepository.findByCode(userCreateDto.getUniversity().getCode()).isPresent()) {
            baseResponse.setStatus(400);
            baseResponse.setMessage("Mã trường đã tồn tại");
            return baseResponse;
        }
        if (universityRepository.findByName(userCreateDto.getUniversity().getName()).isPresent()) {
            baseResponse.setStatus(400);
            baseResponse.setMessage("Tên trường đã tồn tại");
            return baseResponse;
        }
        User user = userService.createUser(userCreateDto);

        if (user.getRole() == RoleType.UNIVERSITY) {
            University university = universityService.createEmptyUniversity(user);
            universityService.updateByUniversityId(university.getId(), userCreateDto.getUniversity());
        }

        baseResponse.setData(modelMapper.map(user, UserDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<Map<String, Object>> login(LoginRequestDto loginRequestDto) {
        BaseResponse<Map<String, Object>> baseResponse = new BaseResponse<>();
        User user = userService.getUserByEmail(loginRequestDto.getEmail());
        if (user == null) {
            baseResponse.setStatus(400);
            baseResponse.setMessage("Tài khoản không tồn tại");
            return baseResponse;
        }
        if (!user.getEnabled()) {
            baseResponse.setStatus(400);
            baseResponse.setMessage("Tài khoản đã bị vô hiệu hóa");
            return baseResponse;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  Return JWT
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        baseResponse.setData(Map.of(
                "tokenType", "Bearer",
                "accessToken", jwt,
                "user", modelMapper.map(user, UserDto.class)
        ));
        baseResponse.success();
        return baseResponse;
//        return new JsonWebTokenModel("Bearer", jwt);
    }


    public BaseResponse<UserDto> me(HttpServletRequest request) throws Exception {
        Long id = tokenProvider.getUserIdFromRequest(request);
        return userService.getUserById(id);
    }
}
