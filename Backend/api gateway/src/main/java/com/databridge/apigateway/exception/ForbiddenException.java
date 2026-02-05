package com.databridge.apigateway.exception;

import com.databridge.apigateway.enums.ErrorCode;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN.getMessage());
    }
}
