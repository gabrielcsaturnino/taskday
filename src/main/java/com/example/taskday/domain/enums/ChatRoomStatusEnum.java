package com.example.taskday.domain.enums;

public enum ChatRoomStatusEnum {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String status;


    ChatRoomStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
