package com.example.taskday.job.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.taskday.chatroom.ChatRoom;
import com.example.taskday.chatroom.enums.ChatRoomStatusEnum;
import com.example.taskday.chatroom.service.ChatRoomService;
import com.example.taskday.job.JobExecution;
import com.example.taskday.job.repository.JobExecutionRepository;
import com.example.taskday.job.service.JobExecutionService;
import com.example.taskday.user.service.ClientService;

@Component
public class JobApplicationAcceptedListener {
    @Autowired
    JobExecutionService jobExecutionService;

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ClientService clientService;


    @EventListener
    public void onJobApplicationAccepted(JobApplicationAcceptedEvent event) {
        if(jobExecutionService.existsByJobId(event.getJobApplication().getJob().getId())) {
            jobExecutionService.addNewContractor(jobExecutionService.findJobExecutionByJobId(event.getJobApplication().getJob().getId()).getId(),event.getJobApplication().getContractor().getId());
            return;
        }
        
        ChatRoom chatRoom = new ChatRoom(event.getJobApplication().getJob().getClient(), event.getJobApplication().getContractor());
        chatRoomService.createChatRoom(chatRoom);
        chatRoomService.updateStatus(chatRoom.getId(), ChatRoomStatusEnum.ACTIVE);
        jobExecutionService.createJobExecution(event.getJobApplication().getId());

    }
}
