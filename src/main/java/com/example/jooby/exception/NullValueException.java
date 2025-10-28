package com.example.jooby.exception;

public class NullValueException extends ServiceException {
    public NullValueException() {
        super("Null value encountered");
    }

    public NullValueException(String message) {
        super(message);
    }

}
