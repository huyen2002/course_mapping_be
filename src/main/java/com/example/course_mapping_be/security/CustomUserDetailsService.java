package com.example.course_mapping_be.security;

import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email is not exist"));
        return new CustomUserDetails(user);
    }


    public UserDetails loadUserById(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User with id is not found"));
        return new CustomUserDetails(user);
    }
}
