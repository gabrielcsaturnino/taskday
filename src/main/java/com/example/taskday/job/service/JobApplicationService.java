package com.example.taskday.job.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.taskday.exception.InvalidStatusException;
import com.example.taskday.exception.NotFoundException;
import com.example.taskday.job.Job;
import com.example.taskday.job.JobApplication;
import com.example.taskday.job.enums.JobApplicationStatusEnum;
import com.example.taskday.job.event.JobApplicationAcceptedEvent;
import com.example.taskday.job.repository.JobApplicationRepository;
import com.example.taskday.job.repository.JobRepository;
import com.example.taskday.user.Contractor;
import com.example.taskday.user.repository.ContractorRepository;

@Service
public class JobApplicationService {
    
    
    private JobApplicationRepository jobApplicationRepository;

    
    private ContractorRepository contractorRepository;

    
    private JobRepository jobRepository;

    
    private ApplicationEventPublisher applicationEventPublisher;


    public JobApplicationService(JobApplicationRepository jobApplicationRepository, ContractorRepository contractorRepository, JobRepository jobRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.contractorRepository = contractorRepository;
        this.jobRepository = jobRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }




    /**
     * Updates the status of a job application.
     *
     * @param jobApplicationId the ID of the job application to update
     * @param status the new status to set
     * @throws NotFoundException if the job application is not found
     */
    public void updateStatus(Long jobApplicationId, JobApplicationStatusEnum status) {
        
        JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new NotFoundException("Job application not found with id: " + jobApplicationId));

        
        if (status == null) {
            throw new InvalidStatusException("Status cannot be null");
        }

        if (jobApplication.getStatusApplication() == status) {
            return;
        }

        if(jobApplication.getStatusApplication() == JobApplicationStatusEnum.ACCEPTED && status != JobApplicationStatusEnum.ACCEPTED) {
            throw new InvalidStatusException("Cannot change status from ACCEPTED to another status.");
        }

        if(jobApplication.getStatusApplication() == JobApplicationStatusEnum.REJECTED && status != JobApplicationStatusEnum.REJECTED) {
            throw new InvalidStatusException("Cannot change status from REJECTED to another status.");
        }
        

        jobApplication.setStatusApplication(status);
        jobApplicationRepository.save(jobApplication);
        if(status == JobApplicationStatusEnum.ACCEPTED) {
            applicationEventPublisher.publishEvent(new JobApplicationAcceptedEvent(this, jobApplication));
        }
    }

    /**
     * Creates a new job application for a contractor.
     *
     * @param jodId the ID of the job to apply for
     * @param contractorId the ID of the contractor applying for the job
     * @throws NotFoundException if the job or contractor is not found
     */
    public JobApplication createJobApplication(Long jodId, Long contractorId) {
        Job job = jobRepository.findById(jodId)
                .orElseThrow(() -> new NotFoundException("Job not found with id: " + jodId));
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new NotFoundException("Contractor not found with id: " + contractorId));
       
        
        JobApplication jobApplication = new JobApplication(job, contractor);
        jobApplicationRepository.save(jobApplication);
        return jobApplication;
    }

    public JobApplication findById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job application not found with id: " + id));
    }

}