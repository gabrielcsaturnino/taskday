package com.example.taskday.job.event;

import org.springframework.context.ApplicationEvent;

import com.example.taskday.job.JobExecution;

public class JobExecutionChangeLeaderEvent extends ApplicationEvent {
    private final JobExecution jobExecution;
    private final Long newLeaderId;

    public JobExecutionChangeLeaderEvent(Object source, JobExecution jobExecution, Long newLeaderId) {
        super(source);
        this.jobExecution = jobExecution;
        this.newLeaderId = newLeaderId;
    }

    public JobExecution getJobExecution() {
        return jobExecution;
    }

    public Long getNewLeaderId() {
        return newLeaderId;
    }
}


