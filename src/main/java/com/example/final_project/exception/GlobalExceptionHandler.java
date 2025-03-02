package com.example.final_project.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleException(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionBody.builder()
                .message(List.of(ex.getMessage()))
                .endpoint(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionBody.builder()
                .message(List.of(ex.getMessage()))
                .endpoint(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionBody.builder()
                .message(List.of(ex.getMessage()))
                .endpoint(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleException(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionBody.builder()
                .message(List.of(ex.getMessage()))
                .endpoint(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionBody.builder()
                .message(ex.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList())
                .endpoint(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionBody.builder()
                .message(List.of(ex.getMessage()))
                .endpoint(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }


    //-----------------------------------Custom exceptions-----------------------------------
    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleCustomException(CustomException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.getStatus())
                .body(ExceptionBody.builder()
                        .message(List.of(ex.getMessage()))
                        .endpoint(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}
