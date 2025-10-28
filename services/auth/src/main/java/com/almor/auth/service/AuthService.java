package com.almor.auth.service;

import com.almor.auth.dto.request.LoginDto;
import com.almor.auth.dto.response.LoginResponseDto;

public interface AuthService {
    public LoginResponseDto login(LoginDto data);
}
