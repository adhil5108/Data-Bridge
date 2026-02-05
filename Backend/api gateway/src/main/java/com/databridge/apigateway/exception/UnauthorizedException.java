package com.databridge.apigateway.exception;

import com.databridge.apigateway.enums.ErrorCode;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super(ErrorCode.MISSING_AUTH_HEADER.getMessage());
    }
}
