package com.example.taskday.message.enums;

public enum TypeMessageEnum {
    TEXT("text"),
    IMAGE("image");

    private final String type;

     TypeMessageEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    
}
