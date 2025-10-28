package com.example.jooby.chatroom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jooby.chatroom.ChatRoom;
import com.example.jooby.chatroom.enums.ChatRoomStatusEnum;
import com.example.jooby.chatroom.repository.ChatRoomRepository;
import com.example.jooby.exception.NotFoundException;
import com.example.jooby.job.Job;
import com.example.jooby.user.Client;
import com.example.jooby.user.Contractor;
import com.example.jooby.user.service.ContractorService;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ContractorService contractorService;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ContractorService contractorService) {
        this.contractorService = contractorService;
        this.chatRoomRepository = chatRoomRepository;
    }


    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat room not found with id: " + id));
    }

    public ChatRoom createChatRoom(Client client, Contractor contractor, Job job) {
        if (client == null || contractor == null || job == null) {
            throw new NotFoundException("Client or Contractor or Job cannot be null");
        }
        
        
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setClient(client);
        chatRoom.setContractor(contractor);
        chatRoom.setJob(job);
        chatRoom.setChatRoomStatusEnum(ChatRoomStatusEnum.ACTIVE);
        
        return chatRoomRepository.save(chatRoom);
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
    
    public List<ChatRoom> findAllByClientId(Long clientId) {
        return chatRoomRepository.findByClientId(clientId);
    }
    
    public List<ChatRoom> findAllByContractorId(Long contractorId) {
        return chatRoomRepository.findByContractorId(contractorId);
    }

    public ChatRoom findByJobId(Long jobId) {
        return chatRoomRepository.findByJobId(jobId)
                .orElseThrow(() -> new NotFoundException("Chat room not found for job id: " + jobId));
    }

    public boolean existsByJobId(Long jobId) {
        return chatRoomRepository.existsByJobId(jobId);
    }

    public void updateLeader(Long chatRoomId, Long newLeaderId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException("Chat room not found with id: " + chatRoomId));
        
        Contractor newLeader = contractorService.findById(newLeaderId);        
        chatRoom.setContractor(newLeader);
        chatRoomRepository.save(chatRoom);

    }
}
