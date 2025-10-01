package com.example.taskday.exception;

public class InvalidPermissionException extends ServiceException {
    public InvalidPermissionException(){
        super("Invalid permission");
    }

    public InvalidPermissionException(String message) {
        super(message);
    }
}
