package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.BaseResponse;
import com.example.course_mapping_be.dtos.QueryParams;
import com.example.course_mapping_be.dtos.UserCreateDto;
import com.example.course_mapping_be.dtos.UserDto;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    public User createUser(UserCreateDto userCreateDto) {
        if (userRepository.findByEmail(userCreateDto.getEmail()).isPresent()) {
            throw new Error("Email is existed");
        }
        User user = new User(userCreateDto.getEmail(), passwordEncoder.encode(userCreateDto.getPassword()), userCreateDto.getName(), userCreateDto.getRole());
        return userRepository.saveAndFlush(user);
    }

    public BaseResponse<UserDto> getUserById(Long id) {
        BaseResponse<UserDto> baseResponse = new BaseResponse<>();
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return new BaseResponse<>("User id is not found", HttpStatus.NOT_FOUND.value());
        }
        baseResponse.setData(modelMapper.map(user.get(), UserDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<List<UserDto>> getAll(QueryParams params) {
        Page<User> users = userRepository.findAll(PageRequest.of(params.getPage(), params.getSize()));
        List<UserDto> userDtoList = users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
        BaseResponse<List<UserDto>> baseResponse = new BaseResponse<>();
        baseResponse.setData(userDtoList);
        baseResponse.updatePagination(params, users.getTotalElements());
        baseResponse.success();
        return baseResponse;
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public BaseResponse<UserDto> update(Long id, UserDto userDto) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User with id is not found"));
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.isEnabled() != user.isEnabled()) {
            user.setEnabled(userDto.isEnabled());
        }
        userRepository.save(user);
        BaseResponse<UserDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(user, UserDto.class));
        baseResponse.success();
        return baseResponse;
    }
}
