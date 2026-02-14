package com.databridge.auth_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @Email
    private String email;

    @Size(min = 8)
    private String password;
}
