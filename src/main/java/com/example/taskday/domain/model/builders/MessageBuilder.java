package com.example.taskday.domain.model.builders;

import com.example.taskday.domain.enums.MessageOwnerEnum;
import com.example.taskday.domain.enums.TypeMessageEnum;
import com.example.taskday.domain.model.ChatRoom;
import com.example.taskday.domain.model.Message;
import com.example.taskday.domain.model.dtos.MessageDTO;

public class MessageBuilder {
    private ChatRoom chatRoom;
    private String contentMessage;
    private TypeMessageEnum typeMessage;
    private MessageOwnerEnum owner;

    public MessageBuilder fromDTO(MessageDTO dto, ChatRoom chatRoom, MessageOwnerEnum owner) {
        this.chatRoom = chatRoom;
        this.contentMessage = dto.getContentMessage();
        if(dto.getTypeMessage() == "text") {
            this.typeMessage = TypeMessageEnum.TEXT;
        } else if(dto.getTypeMessage() == "image") {
            this.typeMessage = TypeMessageEnum.IMAGE;
        }
        this.owner = owner;
        return this;
    }

    public Message build() {
        return new Message(chatRoom, contentMessage, typeMessage, owner);

    }

}
