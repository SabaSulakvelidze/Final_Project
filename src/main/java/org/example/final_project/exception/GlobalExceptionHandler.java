package org.example.final_project.exception;

import jakarta.servlet.http.HttpServletRequest;
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
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleException(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleException(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }


    //-----------------------------------Custom exceptions-----------------------------------
    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleException(InvalidUserException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleUserPermissionException(UserPermissionException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleUserPermissionException(OutOfStockException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionBody exceptionBody = new ExceptionBody(List.of(ex.getMessage()), request.getRequestURI(), LocalDateTime.now(), httpStatus, List.of(ex.getStackTrace()).getLast());
        return ResponseEntity.status(httpStatus).body(exceptionBody);
    }
}
