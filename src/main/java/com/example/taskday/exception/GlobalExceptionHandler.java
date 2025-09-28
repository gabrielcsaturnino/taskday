// Novo arquivo: src/main/java/com/example/taskday/domain/exception/GlobalExceptionHandler.java
package com.example.taskday.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.micrometer.core.ipc.http.HttpSender.Response;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(PasswordException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    @ExceptionHandler(ApplyingJobException.class)
    public ResponseEntity<Map<String, String>> handleApplyingJobException(ApplyingJobException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidStatusException(InvalidStatusException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CreateJobExecutionException.class)
    public ResponseEntity<Map<String, String>> handleCreateJobExecutionException(CreateJobExecutionException ex) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(SaveNullObjectException.class)
    public ResponseEntity<Map<String, String>> handleSaveNullObjectException(SaveNullObjectException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateFieldException(DuplicateFieldException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NullValueException.class)
    public ResponseEntity<Map<String, String>> handleNullValueException(NullValueException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

}