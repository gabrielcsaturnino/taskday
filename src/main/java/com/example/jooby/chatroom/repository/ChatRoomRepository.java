package com.example.jooby.chatroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jooby.chatroom.ChatRoom;
import com.example.jooby.user.Client;
import com.example.jooby.user.Contractor;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findById(Long id);
    boolean existsByJobId(Long jobId);
    List<ChatRoom> findByClientId(Long clientId);
    List<ChatRoom> findByContractorId(Long contractorId);
    List<ChatRoom> findByClientAndContractor(Client client, Contractor contractor);
    Optional<ChatRoom> findByJobId(Long jobId);
}
