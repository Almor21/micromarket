package com.almor.user.consumer;

import com.almor.common.message.CreateUserMessage;
import com.almor.common.message.DeleteUserMessage;
import com.almor.common.message.UpdateUserMessage;
import com.almor.user.dto.request.CreateUserDto;
import com.almor.user.dto.request.UpdateUserDto;
import com.almor.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class UserConsumer {

    private final UserService userService;

    @KafkaListener(topics = "user.created")
    public void createUserConsumer(CreateUserMessage createUserMessage) {
        LocalDateTime createdAt = Instant.ofEpochMilli(createUserMessage.getCreatedAt())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        CreateUserDto data = CreateUserDto.builder()
                .id(createUserMessage.getId())
                .username(createUserMessage.getUsername())
                .email(createUserMessage.getEmail())
                .firstName(createUserMessage.getFirstName())
                .lastName(createUserMessage.getLastName())
                .createdAt(createdAt)
                .build();

        userService.create(data);
    }

    @KafkaListener(topics = "user.updated")
    public void updateUserConsumer(UpdateUserMessage updateUserMessage) {
        UpdateUserDto data = UpdateUserDto.builder()
                .email(updateUserMessage.getEmail())
                .firstName(updateUserMessage.getFirstName())
                .lastName(updateUserMessage.getLastName())
                .enabled(updateUserMessage.getEnable())
                .build();

        userService.update(updateUserMessage.getId(), data);
    }

    @KafkaListener(topics = "user.deleted")
    public void deleteUserConsumer(DeleteUserMessage deleteUserMessage) {
        userService.delete(deleteUserMessage.getId());
    }

}
