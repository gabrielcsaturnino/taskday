package com.example.taskday.job.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskday.auxiliary.Email;
import com.example.taskday.job.JobApplication;
import com.example.taskday.job.enums.JobApplicationStatusEnum;
import com.example.taskday.job.service.JobApplicationService;
import com.example.taskday.user.Contractor;
import com.example.taskday.user.CustomUserDetails;
import com.example.taskday.user.service.ContractorService;

@RestController
@RequestMapping("/api/v1/job-applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final ContractorService contractorService;

    public JobApplicationController(JobApplicationService jobApplicationService, ContractorService contractorService) {
        this.jobApplicationService = jobApplicationService;
        this.contractorService = contractorService;
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<JobApplication> applyForJob(@PathVariable Long jobId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Contractor contractor = contractorService.findContractorByEmail(new Email(userDetails.getUsername()));
        
        JobApplication jobApplication = jobApplicationService.createJobApplication(jobId, contractor.getId());
        return new ResponseEntity<>(jobApplication, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getJobApplicationById(@PathVariable Long id) {
        JobApplication jobApplication = jobApplicationService.findById(id);
        return ResponseEntity.ok(jobApplication);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateJobApplicationStatus(
            @PathVariable Long id,
            @RequestParam JobApplicationStatusEnum status) {
        
        jobApplicationService.updateStatus(id, status);
        return ResponseEntity.ok("Job application status updated successfully");
    }
}