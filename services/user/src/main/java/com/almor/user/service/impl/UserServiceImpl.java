package com.almor.user.service.impl;

import com.almor.user.dto.request.CreateUserDto;
import com.almor.user.dto.request.UpdateUserDto;
import com.almor.user.entity.User;
import com.almor.user.repository.UserRepository;
import com.almor.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User create(CreateUserDto data) {
        User user = User.builder()
                .id(data.getId())
                .username(data.getUsername())
                .email(data.getEmail())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .createdAt(data.getCreatedAt())
                .build();

        return userRepository.save(user);
    }

    @Override
    public User update(String id, UpdateUserDto data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(data.getEmail());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setEnabled(data.isEnabled());

        return userRepository.save(user);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
