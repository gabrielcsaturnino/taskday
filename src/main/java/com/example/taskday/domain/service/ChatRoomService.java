package com.example.taskday.domain.service;

import org.springframework.stereotype.Service;

import com.example.taskday.domain.exception.NotFoundException;
import com.example.taskday.domain.model.ChatRoom;
import com.example.taskday.domain.repositories.ChatRoomRepository;

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

    
}
