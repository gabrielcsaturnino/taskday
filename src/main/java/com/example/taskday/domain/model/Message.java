package com.example.taskday.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.taskday.domain.enums.TypeMessageEnum;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_chat_room", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_contractor", nullable = false)
    private Contractor contractor;

    @Column(name = "content_message", nullable = false, columnDefinition = "TEXT")
    private String contentMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_message", nullable = false)
    private TypeMessageEnum typeMessage = TypeMessageEnum.TEXT;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    // Getters and setters
    public Long getId() { return id; }
    public ChatRoom getChatRoom() { return chatRoom; }
    public void setChatRoom(ChatRoom chatRoom) { this.chatRoom = chatRoom; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public Contractor getContractor() { return contractor; }
    public void setContractor(Contractor contractor) { this.contractor = contractor; }
    public String getContentMessage() { return contentMessage; }
    public void setContentMessage(String contentMessage) { this.contentMessage = contentMessage; }
    public TypeMessageEnum getTypeMessage() { return typeMessage; }
    public void setTypeMessage(TypeMessageEnum typeMessage) { this.typeMessage = typeMessage; }
    public LocalDateTime getCreatedAt() { return created_at; }
    public void setCreatedAt(LocalDateTime createdAt) { this.created_at = createdAt; }
    public LocalDateTime getUpdatedAt() { return updated_at; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updated_at = updatedAt; }
}