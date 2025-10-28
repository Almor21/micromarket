package com.almor.auth.service.impl;

import com.almor.auth.client.KeycloakClient;
import com.almor.auth.dto.request.LoginDto;
import com.almor.auth.dto.response.LoginResponseDto;
import com.almor.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KeycloakClient keycloakClient;

    @Override
    public LoginResponseDto login(LoginDto data) {
        String token = keycloakClient.getToken(data.getUsername(), data.getPassword());

        return LoginResponseDto.builder()
                .token(token)
                .build();
    }
}
