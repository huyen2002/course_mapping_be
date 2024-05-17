package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.repositories.UserRepository;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
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
        if (userRepository.findByUsername(userCreateDto.getUsername()).isPresent()) {
            throw new Error("Username is existed");
        }
        User user = new User(userCreateDto.getEmail(), passwordEncoder.encode(userCreateDto.getPassword()), userCreateDto.getUsername(), userCreateDto.getRole());
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
            if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
                throw new Exception("Username is existed");
            }
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null) {
            if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
                throw new Exception("Email is existed");
            }
            user.setEmail(userDto.getEmail());
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

    public BaseResponse<List<UserDto>> search(SearchUserParams searchUserParams, QueryParams params) {
        BaseResponse<List<UserDto>> baseResponse = new BaseResponse<>();
        Page<User> users = userRepository.searchUsers(searchUserParams.getRole(), searchUserParams.getCreateYear(), searchUserParams.getCreateMonth(), searchUserParams.getUsername(), PageRequest.of(params.getPage(), params.getSize()));
        List<UserDto> userDtoList = users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
        baseResponse.setData(userDtoList);
        baseResponse.updatePagination(params, users.getTotalElements());
        baseResponse.success();
        return baseResponse;
    }

    public BaseResponse<Boolean> changePassword(Long userId, ChangePasswordDto changePasswordDto) throws Exception {
        BaseResponse<Boolean> baseResponse = new BaseResponse<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User is not found"));
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            baseResponse.setData(false);
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("Old password is incorrect");
            return baseResponse;
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        baseResponse.setData(true);
        baseResponse.success();
        return baseResponse;

    }
}
