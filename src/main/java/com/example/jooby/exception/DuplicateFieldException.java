package com.example.jooby.exception;

public class DuplicateFieldException extends ServiceException {
    public DuplicateFieldException(){
        super("Duplicate field");
    }

    public DuplicateFieldException(String message) {
        super(message);
    }

}
