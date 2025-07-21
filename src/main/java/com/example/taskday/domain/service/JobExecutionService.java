package com.example.taskday.domain.service;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskday.domain.enums.JobApplicationStatusEnum;
import com.example.taskday.domain.enums.JobExecutionStatusEnum;
import com.example.taskday.domain.exception.InvalidStatusException;
import com.example.taskday.domain.exception.NotFoundException;
import com.example.taskday.domain.model.Job;
import com.example.taskday.domain.model.JobApplication;
import com.example.taskday.domain.model.JobExecution;
import com.example.taskday.domain.model.auxiliary.Rating;
import com.example.taskday.domain.repositories.JobApplicationRepository;
import com.example.taskday.domain.repositories.JobExecutionRepository;

@Service
public class JobExecutionService {

    @Autowired
    private JobExecutionRepository jobExecutionRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    /**
     * create the JobExecution by jobApplication
     * @param jobApplicationId for create new JobExecution
     * @throws NotFoundException if the jobApplication is not found
     * @throws IllegalArgumentException if jobApplication status violates business rules
    */
    public JobExecution createJobExecution(Long jobApplicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new NotFoundException("JobApllication not found with id: " + jobApplicationId));
        
        if (jobApplication.getStatusApplication() != JobApplicationStatusEnum.ACCEPTED) {
            throw new InvalidStatusException("Job application must be accepted to create a job execution.");
        }

        JobExecution jobExecution = new JobExecution(jobApplication);
        jobExecutionRepository.save(jobExecution);
        return jobExecution;
        
    } 
    
    
    /**
     * Updates the status of a JobExecution.
     * 
     * @param jobExecution the JobExecution to update
     * @param newStatus    the new status to set
     */
    public void updateStatus(Long jobExecutionId, JobExecutionStatusEnum newStatus) {
        
        JobExecution jobExecution = jobExecutionRepository.findById(jobExecutionId)
                .orElseThrow(() -> new NotFoundException("JobExecution not found with id: " + jobExecutionId));
        
        if (jobExecution.getStatus() == JobExecutionStatusEnum.COMPLETED) {
            throw new InvalidStatusException("Cannot change status from COMPLETED to another status."); 
        }
        
        if (jobExecution.getStatus() == newStatus) {
            return;
        }
        if (jobExecution.getStatus() == JobExecutionStatusEnum.CANCELLED && newStatus != JobExecutionStatusEnum.CANCELLED) {
            throw new InvalidStatusException("Cannot change status from CANCELLED to another status.");
        }

        if (jobExecution.getStatus() == JobExecutionStatusEnum.PENDING && newStatus == JobExecutionStatusEnum.COMPLETED) {
            throw new InvalidStatusException("Cannot change status from PENDING to COMPLETED directly.");
            
        }

        jobExecution.setStatus(newStatus);
        switch (newStatus) {
            case IN_PROGRESS -> jobExecution.setIn_progress_at(LocalDateTime.now());
            case COMPLETED -> {
                jobExecution.setCompleted_at(LocalDateTime.now());
                jobExecution.setStatusExecution(true);
                if (jobExecution.getIn_progress_at() != null) {
                    long seconds = java.time.Duration.between(jobExecution.getIn_progress_at(), jobExecution.getCompleted_at()).getSeconds();
                    jobExecution.setTotalTime(BigInteger.valueOf(seconds));
                }}
            case CANCELLED -> {
                jobExecution.setStatusExecution(false);
                jobExecution.setRating(0);
            }
            
        }
    }
    



    /**
     * Updates the average rating of a JobExecution.
     * 
     * @param jobExecution the JobExecution to update
     * @param rating the new average rating to set
     */
    public void updateAvarageRating(Long jobExecutionId, double rating) {
        
        JobExecution jobExecution = jobExecutionRepository.findById(jobExecutionId)
                  .orElseThrow(() -> new NotFoundException("JobExecution not found with id: " + jobExecutionId));
        
        if (rating < 0 || rating > 5) {
            throw new InvalidStatusException("Average rating must be between 0 and 5");
        }
        
        if (jobExecution.getStatus() != JobExecutionStatusEnum.COMPLETED) {
            throw new InvalidStatusException("Job execution must be completed to update the average rating.");    
        }
        Rating newRating = new Rating(rating);
        jobExecution.getContractor().setAvarageRating(newRating);
        jobExecution.setRating(rating);
    }
}