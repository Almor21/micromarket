package com.almor.user.controller;

import com.almor.user.mapper.UserMapper;
import com.almor.user.dto.request.CreateUserDto;
import com.almor.user.dto.response.UserResponseDto;
import com.almor.user.dto.request.UpdateUserDto;
import com.almor.user.entity.User;
import com.almor.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody CreateUserDto data) {
        User user = userService.create(data);
        return UserMapper.toResponse(user);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserDto data) {
        User user = userService.update(id, data);
        return UserMapper.toResponse(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.delete(id);
    }
}
