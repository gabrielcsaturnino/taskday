package com.example.taskday.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskday.domain.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findById(Long id);

}
