package com.almor.auth.controller;

import com.almor.auth.dto.response.LoginResponseDto;
import com.almor.auth.dto.request.LoginDto;
import com.almor.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginDto data) {
        return authService.login(data);
    }

}
