package com.example.taskday.domain.enums;

public enum MessageOwnerEnum {
    CLIENT("client_owner"),
    CONTRACTOR("contractor_owner");

    private final String owner;

    MessageOwnerEnum(String owner) {
        this.owner = owner;
    }
}
