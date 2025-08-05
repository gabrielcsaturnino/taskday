package com.example.taskday.chatroom.controller;
import com.example.taskday.chatroom.ChatRoom;
import com.example.taskday.chatroom.enums.ChatRoomStatusEnum;
import com.example.taskday.chatroom.repository.ChatRoomRepository;
import com.example.taskday.message.Message;
import com.example.taskday.message.builder.MessageBuilder;
import com.example.taskday.message.dto.MessageDTO;
import com.example.taskday.message.enums.MessageOwnerEnum;
import com.example.taskday.message.enums.TypeMessageEnum;
import com.example.taskday.message.service.MessageService;
import com.example.taskday.user.Client;
import com.example.taskday.user.CustomUserDetails;
import com.example.taskday.user.User;
import com.example.taskday.user.repository.ClientRepository;
import com.example.taskday.user.repository.ContractorRepository;
import com.example.taskday.user.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService; 
    private final ChatRoomRepository chatRoomRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final ContractorRepository contractorRepository;
    private final ClientRepository clientRepository;

    public ChatController(ContractorRepository contractorRepository,
                          SimpMessagingTemplate messagingTemplate,
                            UserDetailsServiceImpl userDetailsService,
                          MessageService messageService,
                          ChatRoomRepository chatRoomRepository, ClientRepository clientRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.chatRoomRepository = chatRoomRepository;
        this.contractorRepository = contractorRepository;
        this.clientRepository = clientRepository;
        this.userDetailsService = userDetailsService;
    }

    @MessageMapping("/chat.sendMessage")
    @Transactional
    public void sendMessage(@Payload MessageDTO messageDTO, Principal principal) {
        if (principal == null) {
            System.err.println("Internal error: principal is null.");
            return;
        }

        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Long currentUserId = userDetails.getUserId(); 

        ChatRoom chatRoom = chatRoomRepository.findById(messageDTO.getChatRoomId())
                .orElse(null);

        if (chatRoom == null) {
            System.err.println("Error: chat room not found.");
            return;
        }


        boolean isParticipant = chatRoom.getClient().getId().equals(currentUserId) ||
                                chatRoom.getContractor().getId().equals(currentUserId);

        if (!isParticipant) {
            System.err.println("Error: Denied acess");
            return;
        }

        if (chatRoom.getChatRoomStatusEnum() != ChatRoomStatusEnum.ACTIVE) {
            System.err.println("Error: chat room is not active.");
            return;
        }
        

        if(contractorRepository.findById(currentUserId).isEmpty()) {
            messageDTO.setOwner("client_owner");
        }

        if(clientRepository.findById(currentUserId).isEmpty()) {
           messageDTO.setOwner("contractor_owner");
        }
        

        messageDTO.setTypeMessage("text");
        messageService.saveMessage(messageDTO);
        String destination = "/topic/chat/" + chatRoom.getId();
        messagingTemplate.convertAndSend(destination, messageDTO);
    }
}
