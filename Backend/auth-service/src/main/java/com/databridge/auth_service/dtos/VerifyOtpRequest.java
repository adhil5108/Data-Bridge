package com.databridge.auth_service.dtos;

import lombok.Data;

@Data
public class VerifyOtpRequest {

    private String email;
    private String otp;
}
