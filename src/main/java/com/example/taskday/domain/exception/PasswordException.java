package com.example.taskday.domain.exception;

public class PasswordException extends ServiceException {
    public PasswordException() {
        super("Bad credentials");
    }
    public PasswordException(String message) {
        super(message);
    }
}