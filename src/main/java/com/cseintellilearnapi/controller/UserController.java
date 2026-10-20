package com.cseintellilearnapi.controller;

import com.cseintellilearnapi.dto.user.UserResponse;
import com.cseintellilearnapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
