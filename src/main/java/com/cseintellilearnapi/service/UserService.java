package com.cseintellilearnapi.service;

import com.cseintellilearnapi.dto.user.UserResponse;
import com.cseintellilearnapi.exception.ResourceNotFoundException;
import com.cseintellilearnapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
