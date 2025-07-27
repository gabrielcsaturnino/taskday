package com.example.taskday.domain.events;

import org.springframework.context.ApplicationEvent;

import com.example.taskday.domain.model.JobApplication;
import com.example.taskday.domain.model.JobExecution;

public class JobApplicationAcceptedEvent extends ApplicationEvent {

    private JobApplication jobApplication;
    public JobApplicationAcceptedEvent(Object source, JobApplication jobApplication) {
        super(source);
        this.jobApplication = jobApplication;
    }

   

    public JobApplication getJobApplication() {
        return jobApplication;
    
    }

}
