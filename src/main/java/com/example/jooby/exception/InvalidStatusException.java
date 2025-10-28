package com.example.jooby.exception;

public class InvalidStatusException extends ServiceException {
    public InvalidStatusException(){
        super("Invalid status");
    }

    public InvalidStatusException(String message) {
        super(message);
    }
}
