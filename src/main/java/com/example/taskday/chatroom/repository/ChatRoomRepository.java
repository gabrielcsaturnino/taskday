package com.example.taskday.chatroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskday.chatroom.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findById(Long id);
}
