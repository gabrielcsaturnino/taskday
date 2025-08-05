package com.example.taskday.exception;

public class CreateJobExecutionException extends ServiceException {
    public CreateJobExecutionException() {
        super("Error creating job execution");
    }

    public CreateJobExecutionException(String message) {
        super(message);
    }
} 


