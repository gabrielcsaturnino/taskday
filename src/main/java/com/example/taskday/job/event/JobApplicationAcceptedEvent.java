package com.example.taskday.job.event;

import org.springframework.context.ApplicationEvent;

import com.example.taskday.job.JobApplication;
import com.example.taskday.job.JobExecution;

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
