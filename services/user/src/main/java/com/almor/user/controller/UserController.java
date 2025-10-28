package com.almor.user.controller;

import com.almor.user.dto.response.UserResponseDto;
import com.almor.user.entity.User;
import com.almor.user.mapper.UserMapper;
import com.almor.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponseDto> response = users.stream()
                .map(UserMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResponseDto response = UserMapper.toResponse(user);

        return ResponseEntity.ok(response);
    }

}
