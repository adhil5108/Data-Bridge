package com.databridge.apigateway.exception;

import com.databridge.apigateway.enums.ErrorCode;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN.getMessage());
    }
}
