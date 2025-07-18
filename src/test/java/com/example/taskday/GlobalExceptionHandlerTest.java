package com.example.taskday;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.taskday.domain.exception.GlobalExceptionHandler;
import com.example.taskday.domain.exception.PasswordException;

class GlobalExceptionHandlerTest {
    @Test
    void handlePasswordException() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        PasswordException exception = new PasswordException("Invalid password");

        ResponseEntity <Map<String, String>> response = globalExceptionHandler.handleBadCredentialsException(exception);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid password", response.getBody().get("error"));
    }
    
}
