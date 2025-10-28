package com.example.jooby.message.builder;

import com.example.jooby.chatroom.ChatRoom;
import com.example.jooby.message.Message;
import com.example.jooby.message.dto.MessageDTO;
import com.example.jooby.message.enums.MessageOwnerEnum;
import com.example.jooby.message.enums.TypeMessageEnum;

public class MessageBuilder {
    private ChatRoom chatRoom;
    private String contentMessage;
    private TypeMessageEnum typeMessage;
    private MessageOwnerEnum owner;

    public MessageBuilder fromDTO(MessageDTO dto, ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        this.contentMessage = dto.getContentMessage();
        if(dto.getTypeMessage() == "text") {
            this.typeMessage = TypeMessageEnum.TEXT;
        } else if(dto.getTypeMessage() == "image") {
            this.typeMessage = TypeMessageEnum.IMAGE;
        }

        if (dto.getOwner().equals("client_owner")) {
            this.owner = MessageOwnerEnum.CLIENT;
        } else if (dto.getOwner().equals("contractor_owner")) {
            this.owner = MessageOwnerEnum.CONTRACTOR;
        } else {
            throw new IllegalArgumentException("Invalid owner type: " + owner);
            
        }

        return this;
    }

    public Message build() {
        return new Message(chatRoom, contentMessage, typeMessage, owner);

    }

}
