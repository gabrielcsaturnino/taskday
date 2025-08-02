package com.example.taskday.domain.controller;
import com.example.taskday.domain.enums.ChatRoomStatusEnum;
import com.example.taskday.domain.enums.MessageOwnerEnum;
import com.example.taskday.domain.enums.TypeMessageEnum;
import com.example.taskday.domain.model.ChatRoom;
import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.CustomUserDetails;
import com.example.taskday.domain.model.Message;
import com.example.taskday.domain.model.User;
import com.example.taskday.domain.model.builders.MessageBuilder;
import com.example.taskday.domain.model.dtos.MessageDTO;
import com.example.taskday.domain.repositories.ChatRoomRepository;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.ContractorRepository;
import com.example.taskday.domain.service.MessageService;
import com.example.taskday.domain.service.UserDetailsServiceImpl;

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
            System.err.println("Tentativa de envio de mensagem sem autenticação.");
            return;
        }

        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Long currentUserId = userDetails.getUserId(); 

        ChatRoom chatRoom = chatRoomRepository.findById(messageDTO.getChatRoomId())
                .orElse(null);

        if (chatRoom == null) {
            System.err.println("Tentativa de envio para sala de chat inexistente: " + messageDTO.getChatRoomId());
            return;
        }


        boolean isParticipant = chatRoom.getClient().getId().equals(currentUserId) ||
                                chatRoom.getContractor().getId().equals(currentUserId);

        if (!isParticipant) {
            System.err.println("ACESSO NEGADO: Usuário " + currentUserId + " tentou enviar mensagem para a sala " + chatRoom.getId() + " sem ser participante.");
            return;
        }

        if (chatRoom.getChatRoomStatusEnum() != ChatRoomStatusEnum.ACTIVE) {
            System.err.println("CHAT INATIVO: Mensagem ignorada para a sala " + chatRoom.getId() + ". Status atual: " + chatRoom.getChatRoomStatusEnum());
            return;
        }
        
        MessageOwnerEnum owner = null;

        if(contractorRepository.findById(currentUserId).isEmpty()) {
            owner = MessageOwnerEnum.CLIENT;
        }

        if(clientRepository.findById(currentUserId).isEmpty()) {
           owner = MessageOwnerEnum.CONTRACTOR;
        }
        

        Message savedMessage = messageService.saveMessage(messageDTO, owner);

        String destination = "/topic/chat/" + chatRoom.getId();
        messagingTemplate.convertAndSend(destination, savedMessage);
    }
}
