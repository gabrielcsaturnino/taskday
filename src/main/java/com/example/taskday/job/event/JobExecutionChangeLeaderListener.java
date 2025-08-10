package com.example.taskday.job.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.taskday.chatroom.ChatRoom;
import com.example.taskday.chatroom.service.ChatRoomService;

@Component
public class JobExecutionChangeLeaderListener {
    ChatRoomService chatRoomService;
    public JobExecutionChangeLeaderListener(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }
    @EventListener
    public void onChangeLeader(JobExecutionChangeLeaderEvent event) {
        
        ChatRoom chatRoom = chatRoomService.findByJobId(event.getJobExecution().getJob().getId());
        chatRoomService.updateLeader(chatRoom.getId(), event.getNewLeaderId());
        
    }

}
