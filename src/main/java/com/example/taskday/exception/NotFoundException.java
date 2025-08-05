package com.example.taskday.exception;

public class NotFoundException extends ServiceException {
    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }

}
