package com.example.jooby.message.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jooby.message.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message save(Message message);
    Optional<Message> findById(Long id);
    
    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId ORDER BY m.created_at ASC")
    List<Message> findAllByChatRoomIdOrderByCreatedAtAsc(@Param("chatRoomId") Long chatRoomId);
}
