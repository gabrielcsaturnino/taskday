package com.example.taskday.message.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskday.chatroom.ChatRoom;
import com.example.taskday.chatroom.service.ChatRoomService;
import com.example.taskday.message.Message;
import com.example.taskday.message.builder.MessageBuilder;
import com.example.taskday.message.dto.MessageDTO;
import com.example.taskday.message.enums.MessageOwnerEnum;
import com.example.taskday.message.repository.MessageRepository;

@Service
public class MessageService {


    private final MessageRepository messageRepository;
    private final ChatRoomService chatRoomService;

    public MessageService(MessageRepository messageRepository, ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
        this.messageRepository = messageRepository;
    }

    public Message createMessage(MessageDTO messageDTO) {

        ChatRoom chatRoom = chatRoomService.findById(messageDTO.getChatRoomId());
        Message message = new MessageBuilder()
                .fromDTO(messageDTO, chatRoom)
                .build();
        return messageRepository.save(message);
    }
    
    public List<Message> findAllByChatRoomId(Long chatRoomId) {
        return messageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }
}
