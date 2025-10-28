package com.example.jooby.job.event;

import org.springframework.context.ApplicationEvent;

import com.example.jooby.job.JobApplication;
import com.example.jooby.job.JobExecution;

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
