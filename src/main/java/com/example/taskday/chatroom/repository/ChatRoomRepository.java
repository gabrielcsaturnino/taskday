package com.example.taskday.chatroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskday.chatroom.ChatRoom;
import com.example.taskday.user.Client;
import com.example.taskday.user.Contractor;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findById(Long id);
    boolean existsByJobId(Long jobId);
    List<ChatRoom> findByClientId(Long clientId);
    List<ChatRoom> findByContractorId(Long contractorId);
    List<ChatRoom> findByClientAndContractor(Client client, Contractor contractor);
    Optional<ChatRoom> findByJobId(Long jobId);
}
