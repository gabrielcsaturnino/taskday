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
    JobExecutionService jobExecutionService;

    ChatRoomService chatRoomService;

    ClientService clientService;

    public JobApplicationAcceptedListener(JobExecutionService jobExecutionService, ChatRoomService chatRoomService, ClientService clientService) {
        this.jobExecutionService = jobExecutionService;
        this.chatRoomService = chatRoomService;
        this.clientService = clientService;
    }


    @EventListener
    public void onJobApplicationAccepted(JobApplicationAcceptedEvent event) {
        if(jobExecutionService.existsByJobId(event.getJobApplication().getJob().getId())) {
            jobExecutionService.addNewContractor(jobExecutionService.findJobExecutionByJobId(event.getJobApplication().getJob().getId()).getId(),event.getJobApplication().getContractor().getId());
            return;
        }
        
        if (!chatRoomService.existsByJobId(event.getJobApplication().getJob().getId())) {
            ChatRoom chatRoom = chatRoomService.createChatRoom(event.getJobApplication().getJob().getClient(), event.getJobApplication().getContractor(), event.getJobApplication().getJob());
            chatRoomService.updateStatus(chatRoom.getId(), ChatRoomStatusEnum.ACTIVE);
        }

        jobExecutionService.createJobExecution(event.getJobApplication().getId());

    }
}
