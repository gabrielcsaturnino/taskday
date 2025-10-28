package com.example.jooby.chatroom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jooby.auxiliary.Email;
import com.example.jooby.chatroom.ChatRoom;
import com.example.jooby.chatroom.enums.ChatRoomStatusEnum;
import com.example.jooby.chatroom.service.ChatRoomService;
import com.example.jooby.message.Message;
import com.example.jooby.message.service.MessageService;
import com.example.jooby.user.Client;
import com.example.jooby.user.Contractor;
import com.example.jooby.user.CustomUserDetails;
import com.example.jooby.user.service.ClientService;
import com.example.jooby.user.service.ContractorService;

@RestController
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final ClientService clientService;
    private final ContractorService contractorService;

    public ChatRoomController(ChatRoomService chatRoomService, MessageService messageService,
            ClientService clientService, ContractorService contractorService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
        this.clientService = clientService;
        this.contractorService = contractorService;
    }

  

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoom> getChatRoomById(@PathVariable Long id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        
        ChatRoom chatRoom = chatRoomService.findById(id);
        
        if (!chatRoom.getClient().getId().equals(userId) && !chatRoom.getContractor().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getChatRoomMessages(@PathVariable Long id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        
        ChatRoom chatRoom = chatRoomService.findById(id);
        
        if (!chatRoom.getClient().getId().equals(userId) && !chatRoom.getContractor().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<Message> messages = messageService.findAllByChatRoomId(id);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateChatRoomStatus(
            @PathVariable Long id,
            @RequestParam ChatRoomStatusEnum status,
            Authentication authentication) {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        
        ChatRoom chatRoom = chatRoomService.findById(id);
        
        if (!chatRoom.getClient().getId().equals(userId) && !chatRoom.getContractor().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        chatRoomService.updateStatus(id, status);
        return ResponseEntity.ok("Chat room status updated successfully");
    }

    @GetMapping("/my-chats")
    public ResponseEntity<List<ChatRoom>> getMyChatRooms(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        
        List<ChatRoom> chatRooms;
        if (clientService.existsById(userId)) {
            chatRooms = chatRoomService.findAllByClientId(userId);
        } else {
            chatRooms = chatRoomService.findAllByContractorId(userId);
        }
        
        return ResponseEntity.ok(chatRooms);
    }
}