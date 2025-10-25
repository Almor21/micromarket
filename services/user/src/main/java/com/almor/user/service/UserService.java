package com.almor.user.service;

import com.almor.user.dto.request.CreateUserDto;
import com.almor.user.dto.request.UpdateUserDto;
import com.almor.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(String id);
    User create(CreateUserDto data);
    User update(String id, UpdateUserDto user);
    void delete(String id);
}
