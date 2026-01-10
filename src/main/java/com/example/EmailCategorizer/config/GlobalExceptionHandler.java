package com.example.EmailCategorizer.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.EmailCategorizer.dto.ApiError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ApiError(
                        HttpStatus.BAD_GATEWAY.value(),
                        "Gmail API communication failed",
                        ex.getMessage()));
    }

    @ExceptionHandler(GoogleJsonResponseException.class)
    public ResponseEntity<ApiError> handleGoogleApi(GoogleJsonResponseException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        ex.getStatusCode(),
                        "Google API error",
                        ex.getDetails().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(
                        500,
                        "Unexpected server error",
                        ex.getMessage()));
    }
}
