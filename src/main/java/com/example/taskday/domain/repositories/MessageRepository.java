package com.example.taskday.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskday.domain.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message save(Message message);
    Optional<Message> findById(Long id);
   
}
