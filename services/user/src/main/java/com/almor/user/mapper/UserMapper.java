package com.almor.user.mapper;

import com.almor.user.dto.response.UserResponseDto;
import com.almor.user.entity.User;

public class UserMapper {
    public static UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
