package com.almor.user.consumer;

import com.almor.common.message.CreateUserMessage;
import com.almor.user.dto.request.CreateUserDto;
import com.almor.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserConsumer {

    private final UserService userService;

    @KafkaListener(topics = "user.created")
    public void createUserConsumer(CreateUserMessage createUserMessage) {
        CreateUserDto data = CreateUserDto.builder()
                .id(createUserMessage.getId())
                .username(createUserMessage.getUsername())
                .email(createUserMessage.getEmail())
                .firstName(createUserMessage.getFirstName())
                .lastName(createUserMessage.getLastName())
                .build();

        userService.create(data);
    }

}
