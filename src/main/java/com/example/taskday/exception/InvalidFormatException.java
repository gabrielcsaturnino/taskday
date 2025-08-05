package com.example.taskday.exception;

public class InvalidFormatException extends ServiceException {
    public InvalidFormatException(){
        super("Invalid format");
    }

    public InvalidFormatException(String message){
        super(message);
    }

}
