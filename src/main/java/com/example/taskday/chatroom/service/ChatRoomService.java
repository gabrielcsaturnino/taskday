package com.example.taskday.chatroom.service;

import org.springframework.stereotype.Service;

import com.example.taskday.chatroom.ChatRoom;
import com.example.taskday.chatroom.enums.ChatRoomStatusEnum;
import com.example.taskday.chatroom.repository.ChatRoomRepository;
import com.example.taskday.exception.NotFoundException;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat room not found with id: " + id));
    }

    public void createChatRoom(ChatRoom chatRoom) {
        if (chatRoom == null) {
            throw new NotFoundException("Chat room cannot be null");
        }
        chatRoomRepository.save(chatRoom);
    }

    public void updateStatus(Long chatRoomId, ChatRoomStatusEnum newStatus) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId) 
                .orElseThrow(() -> new NotFoundException("Chat room not found with id: " + chatRoomId));
        if (newStatus == null) {
            throw new NotFoundException("Status cannot be null"); 
        }
        chatRoom.setChatRoomStatusEnum(newStatus);
        chatRoomRepository.save(chatRoom);
    }



     

    
}
