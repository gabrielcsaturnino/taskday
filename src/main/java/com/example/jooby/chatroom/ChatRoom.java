package com.example.jooby.chatroom;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.jooby.chatroom.enums.ChatRoomStatusEnum;
import com.example.jooby.job.Job;
import com.example.jooby.user.Client;
import com.example.jooby.user.Contractor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat_room", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;
    
    @OneToOne
    @JoinColumn(name = "id_job", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "id_contractor", nullable = false)
    private Contractor contractor;

    @Column(name = "status_chat", nullable = false)
    private ChatRoomStatusEnum chatRoomStatusEnum;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    public ChatRoom(Client client, Contractor contractor, Job job) {
        this.job = job;
        this.client = client;
        this.contractor = contractor;
        chatRoomStatusEnum = ChatRoomStatusEnum.INACTIVE;
    }

    public ChatRoom() {
    }

    public void setChatRoomStatusEnum(ChatRoomStatusEnum chatRoomStatusEnum) {
        this.chatRoomStatusEnum = chatRoomStatusEnum;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public ChatRoomStatusEnum getChatRoomStatusEnum() {
        return chatRoomStatusEnum;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
