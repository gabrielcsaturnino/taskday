package com.example.taskday.domain.service;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.taskday.domain.enums.JobExecutionStatusEnum;
import com.example.taskday.domain.model.JobExecution;

@Service
public class JobExecutionService {

    public void updateStatus(JobExecution jobExecution, JobExecutionStatusEnum newStatus) {
        
        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }
        
        if (jobExecution.getStatus() == JobExecutionStatusEnum.COMPLETED) {
            throw new IllegalArgumentException("Cannot change status from COMPLETED to another status."); 
        }
        
        if (jobExecution.getStatus() == newStatus) {
            return;
        }
        if (jobExecution.getStatus() == JobExecutionStatusEnum.CANCELLED && newStatus != JobExecutionStatusEnum.CANCELLED) {
            throw new IllegalArgumentException("Cannot change status from CANCELLED to another status.");
        }

        if (jobExecution.getStatus() == JobExecutionStatusEnum.PENDING && newStatus == JobExecutionStatusEnum.COMPLETED) {
            throw new IllegalArgumentException("Cannot change status from PENDING to COMPLETED directly.");
            
        }

        

        switch (newStatus) {
            case IN_PROGRESS:
                jobExecution.setIn_progress_at(LocalDateTime.now());
                jobExecution.setStatus(newStatus);
                break;
            case COMPLETED:
                jobExecution.setCompleted_at(LocalDateTime.now());
                jobExecution.setStatusExecution(true);
                jobExecution.setStatus(newStatus);
                if (jobExecution.getIn_progress_at() != null) {
                    long seconds = java.time.Duration.between(jobExecution.getIn_progress_at(), jobExecution.getCompleted_at()).getSeconds();
                    jobExecution.setTotalTime(BigInteger.valueOf(seconds));
                }
                break;
            case CANCELLED:
                jobExecution.setStatusExecution(false);
                jobExecution.setAvarageRating(0);
                jobExecution.setStatus(newStatus);
                break;
            default:
                jobExecution.setStatus(newStatus);
        }

        
    }



    public void updateAvarageRating(JobExecution jobExecution, double avarageRating) {
        if (avarageRating < 0 || avarageRating > 5) {
            throw new IllegalArgumentException("Average rating must be between 0 and 5");
        }
        double currentRating = jobExecution.getContractor().getAvarageRating();
        double newRating = (currentRating * 0.8) + (avarageRating * 0.2);
        jobExecution.getContractor().setAvarageRating(newRating);
        jobExecution.setAvarageRating(avarageRating);
    }
}