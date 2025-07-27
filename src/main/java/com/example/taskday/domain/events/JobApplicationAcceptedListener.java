package com.example.taskday.domain.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.taskday.domain.model.JobExecution;
import com.example.taskday.domain.repositories.JobExecutionRepository;
import com.example.taskday.domain.service.JobExecutionService;

@Component
public class JobApplicationAcceptedListener {
    @Autowired
    JobExecutionService jobExecutionService;


    @EventListener
    public void onJobApplicationAccepted(JobApplicationAcceptedEvent event) {
        if(jobExecutionService.existsByJobId(event.getJobApplication().getJob().getId())) {
            jobExecutionService.addNewContractor(jobExecutionService.findJobExecutionByJobId(event.getJobApplication().getJob().getId()).getId(),event.getJobApplication().getContractor().getId());
            return;
        }
        jobExecutionService.createJobExecution(event.getJobApplication().getId());

    }
}
