package com.example.taskday.exception;

public class ServiceException extends RuntimeException{
    public ServiceException(String message) {
        super(message);
    }
}
