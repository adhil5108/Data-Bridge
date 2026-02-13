package com.databridge.auth_service.exception;

import com.databridge.auth_service.dtos.ErrorResponse;
import com.databridge.auth_service.enums.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //custom exceptions
    @ExceptionHandler(AuthException.class)
    public ErrorResponse handleAppException(AuthException ex,
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



    // validations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletResponse response){

        response.setStatus(HttpStatus.BAD_REQUEST.value());

        FieldError fieldError = ex.getBindingResult()
                .getFieldErrors()
                .get(0);

        String message = fieldError.getField() +
                ": " + fieldError.getDefaultMessage();

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .error(ErrorCode.VALIDATION_FAILED.name())
                .message(message)
                .build();
    }
    // unknown errors
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
