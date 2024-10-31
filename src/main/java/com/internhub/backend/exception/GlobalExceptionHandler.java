package com.internhub.backend.exception;

import com.internhub.backend.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        EnumException enumException = EnumException.UNCATEGORIZED_EXCEPTION;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(enumException.getStatusCode().value())
                .message(enumException.getMessage() + e.getClass().getSimpleName() + " - " + e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, enumException.getStatusCode());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        EnumException enumException = e.getEnumException();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(enumException.getStatusCode().value())
                .message(enumException.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, enumException.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(String.join(", ", errorMessages))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException() {
        EnumException enumException = EnumException.UNAUTHORIZED;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(enumException.getStatusCode().value())
                .message(enumException.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, enumException.getStatusCode());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException() {
        EnumException enumException = EnumException.PAGE_NOT_FOUND;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(enumException.getStatusCode().value())
                .message(enumException.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
