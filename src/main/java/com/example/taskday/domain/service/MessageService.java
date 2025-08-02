package com.example.taskday.domain.service;

import org.springframework.stereotype.Service;

import com.example.taskday.domain.enums.MessageOwnerEnum;
import com.example.taskday.domain.model.ChatRoom;
import com.example.taskday.domain.model.Message;
import com.example.taskday.domain.model.builders.MessageBuilder;
import com.example.taskday.domain.model.dtos.MessageDTO;
import com.example.taskday.domain.repositories.MessageRepository;

@Service
public class MessageService {


    private final MessageRepository messageRepository;
    private final ChatRoomService chatRoomService;

    public MessageService(MessageRepository messageRepository, ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(MessageDTO messageDTO, MessageOwnerEnum owner) {

        ChatRoom chatRoom = chatRoomService.findById(messageDTO.getChatRoomId());

        Message message = new MessageBuilder()
                .fromDTO(messageDTO, chatRoom, owner)
                .build();
        return messageRepository.save(message);
    }
}
