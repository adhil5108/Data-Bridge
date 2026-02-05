package com.databridge.auth_service.exception;

import com.databridge.auth_service.dtos.ErrorResponse;
import com.databridge.auth_service.enums.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ErrorResponse handleAppException(AppException ex,
                                            HttpServletResponse response){
        ErrorCode error = ex.getErrorCode();
        response.setStatus(error.getStatus().value());

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .error(error.name())
                .message(error.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleBadCredentials(HttpServletResponse response){

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .error("INVALID_CREDENTIALS")
                .message("Invalid email or password")
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(HttpServletResponse response){

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .error("INTERNAL_SERVER_ERROR")
                .message("Something went wrong")
                .build();
    }

}
