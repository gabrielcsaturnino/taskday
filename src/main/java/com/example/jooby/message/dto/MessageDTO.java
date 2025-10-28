package com.example.jooby.message.dto;

public class MessageDTO {

    private Long chatRoomId;
    private String contentMessage;
    private String typeMessage = "text";
    private String owner;

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public String getOwner() {
        return owner;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }
}
