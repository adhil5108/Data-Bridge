package com.databridge.auth_service.dtos;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class ErrorResponse {

    private Instant timestamp;
    private String error;
    private String message;
}
