package com.example.taskday.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskday.domain.enums.JobApplicationStatusEnum;
import com.example.taskday.domain.exception.InvalidStatusException;
import com.example.taskday.domain.exception.NotFoundException;
import com.example.taskday.domain.model.Contractor;
import com.example.taskday.domain.model.Job;
import com.example.taskday.domain.model.JobApplication;
import com.example.taskday.domain.repositories.ContractorRepository;
import com.example.taskday.domain.repositories.JobApplicationRepository;
import com.example.taskday.domain.repositories.JobRepository;

@Service
public class JobApplicationService {
    
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private JobRepository jobRepository;





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

}
