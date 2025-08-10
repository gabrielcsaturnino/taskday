package com.example.taskday.message.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskday.message.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message save(Message message);
    Optional<Message> findById(Long id);
    List<Message> findAllByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
}
