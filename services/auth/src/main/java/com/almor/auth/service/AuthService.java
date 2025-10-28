package com.almor.auth.service;

import com.almor.auth.dto.request.LoginDto;
import com.almor.auth.dto.request.RegisterDto;
import com.almor.auth.dto.response.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginDto data);
    void register(RegisterDto data);
}
