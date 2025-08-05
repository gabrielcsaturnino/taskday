package com.example.taskday.exception;

public class ApplyingJobException extends ServiceException {
    public ApplyingJobException() {
        super("Error applying for the job");
    }

    public ApplyingJobException(String message) {
        super(message);
    }

}
