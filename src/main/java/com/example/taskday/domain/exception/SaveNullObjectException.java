package com.example.taskday.domain.exception;

public class SaveNullObjectException extends ServiceException {
    public SaveNullObjectException() {
        super("Cannot save a null object");
    }

    public SaveNullObjectException(String message) {
        super(message);
    }

}
