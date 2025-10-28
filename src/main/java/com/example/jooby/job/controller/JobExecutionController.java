package com.example.jooby.job.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jooby.auxiliary.Email;
import com.example.jooby.exception.InvalidPermissionException;
import com.example.jooby.job.JobExecution;
import com.example.jooby.job.enums.JobExecutionStatusEnum;
import com.example.jooby.job.service.JobExecutionService;
import com.example.jooby.user.Contractor;
import com.example.jooby.user.CustomUserDetails;
import com.example.jooby.user.service.ContractorService;

@RestController
@RequestMapping("/api/v1/job-executions")
public class JobExecutionController {

    private final JobExecutionService jobExecutionService;
    private final ContractorService contractorService;

    public JobExecutionController(JobExecutionService jobExecutionService, ContractorService contractorService) {
        this.jobExecutionService = jobExecutionService;
        this.contractorService = contractorService;
    }

    

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateJobExecutionStatus(
            @PathVariable Long id,
            @RequestParam JobExecutionStatusEnum status,
            Authentication authentication
            ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if(jobExecutionService.getLeader(id).getId() != userDetails.getUserId()){
            throw new InvalidPermissionException("Only the leader can perform action");
        }
        jobExecutionService.updateStatus(id, status);
        return ResponseEntity.ok("Job execution status updated successfully");
    }

    @PutMapping("/{id}/rating")
    public ResponseEntity<String> updateJobExecutionRating(
            @PathVariable Long id,
            @RequestParam double rating,
            Authentication authentication
            ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if(jobExecutionService.getClient(id).getId() != userDetails.getUserId()) {
            throw new InvalidPermissionException("Only the creator can perform action");
        }        
        jobExecutionService.updateAverageRating(id, rating);
        return ResponseEntity.ok("Job execution rating updated successfully");
    }

    @PostMapping("/{id}/add-contractor/{contractorId}")
    public ResponseEntity<String> addContractorToJobExecution(
            @PathVariable Long id,
            @PathVariable Long contractorId,
            Authentication authentication
            ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if(jobExecutionService.getClient(id).getId() != userDetails.getUserId()) {
            throw new InvalidPermissionException("Only the creator can perform action");
        }
        jobExecutionService.addNewContractor(id, contractorId);
        return ResponseEntity.ok("Contractor added to job execution successfully");
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<JobExecution> getJobExecutionByJobId(@PathVariable Long jobId) {
        JobExecution jobExecution = jobExecutionService.findJobExecutionByJobId(jobId);
        return ResponseEntity.ok(jobExecution);
    }

    @GetMapping("/completed/contractor")
    public ResponseEntity<List<JobExecution>> getCompletedJobExecutionsByContractor(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Contractor contractor = contractorService.findContractorByEmail(new Email(userDetails.getUsername()));
        
        List<JobExecution> jobExecutions = jobExecutionService.findAllCompletedByContractorId(contractor.getId());
        return ResponseEntity.ok(jobExecutions);
    }
}