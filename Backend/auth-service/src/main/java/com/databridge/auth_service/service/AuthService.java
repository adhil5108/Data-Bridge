package com.databridge.auth_service.service;

import com.databridge.auth_service.dtos.*;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshRequest request);

    AuthResponse verifyOtp(VerifyOtpRequest request);
}
