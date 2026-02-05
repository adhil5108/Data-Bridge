package com.databridge.auth_service.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String role;
}
