package com.almor.auth.controller;

import com.almor.auth.dto.request.RegisterDto;
import com.almor.auth.dto.response.LoginResponseDto;
import com.almor.auth.dto.request.LoginDto;
import com.almor.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto data) {
        LoginResponseDto response = authService.login(data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterDto data) {
        authService.register(data);
    }

}
