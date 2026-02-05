package com.databridge.apigateway.enums;

public enum ErrorCode {

    INVALID_TOKEN("Invalid JWT Token"),
    MISSING_AUTH_HEADER("Missing or invalid Authorization header"),
    FORBIDDEN("You do not have permission to access this resource");

    private final String message;

    ErrorCode(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
