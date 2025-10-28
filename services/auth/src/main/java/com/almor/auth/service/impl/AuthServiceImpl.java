package com.almor.auth.service.impl;

import com.almor.auth.client.KeycloakClient;
import com.almor.auth.client.dto.AdminRegisterUserDto;
import com.almor.auth.dto.request.LoginDto;
import com.almor.auth.dto.request.RegisterDto;
import com.almor.auth.dto.response.LoginResponseDto;
import com.almor.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KeycloakClient keycloakClient;

    @Override
    public LoginResponseDto login(LoginDto data) {
        String token = keycloakClient.getUserToken(data.getUsername(), data.getPassword());

        return LoginResponseDto.builder()
                .token(token)
                .build();
    }

    public void register(RegisterDto data) {
        AdminRegisterUserDto keycloakData = AdminRegisterUserDto.builder()
                .username(data.getUsername())
                .email(data.getEmail())
                .enabled(data.isEnabled())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .credentials(List.of(new AdminRegisterUserDto.CredentialDto(
                        "password",
                        data.getPassword(),
                        false
                )))
                .build();

        keycloakClient.register(keycloakData);
    }
}
