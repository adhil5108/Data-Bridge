package com.databridge.auth_service.dtos;


import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}
