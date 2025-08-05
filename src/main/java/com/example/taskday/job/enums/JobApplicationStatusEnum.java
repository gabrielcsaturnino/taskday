package com.example.taskday.job.enums;

public enum JobApplicationStatusEnum {
    SUBMITTED("submitted"),
    REJECTED("rejected"),
    ACCEPTED("accepted");

    private final String status;

    JobApplicationStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
