package com.databridge.auth_service.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_ALREADY_EXISTS("User already exists", HttpStatus.CONFLICT),

    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),

    INVALID_CREDENTIALS("Invalid email or password", HttpStatus.UNAUTHORIZED),

    INVALID_TOKEN("Invalid or expired token", HttpStatus.UNAUTHORIZED),

    TOKEN_EXPIRED("Token has expired", HttpStatus.UNAUTHORIZED),

    TOKEN_REVOKED("Token has been revoked", HttpStatus.UNAUTHORIZED),

    INVALID_OTP("Invalid OTP", HttpStatus.UNAUTHORIZED),

    REGISTRATION_EXPIRED("Registration expired. Please register again.", HttpStatus.GONE);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
