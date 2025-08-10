package com.example.taskday.job.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskday.auxiliary.Email;
import com.example.taskday.job.Job;
import com.example.taskday.job.dto.CreateJobRequestDTO;
import com.example.taskday.job.service.JobService;
import com.example.taskday.user.Client;
import com.example.taskday.user.CustomUserDetails;
import com.example.taskday.user.service.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;
    private final ClientService clientService;

    public JobController(JobService jobService, ClientService clientService) {
        this.jobService = jobService;
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<String> createJob(@Valid @RequestBody CreateJobRequestDTO createJobDTO, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Client client = clientService.findByEmail(new Email(userDetails.getUsername()));
        
        jobService.createJob(createJobDTO, client);
        return new ResponseEntity<>("Job created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.findById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Job>> getJobsByClientId(@PathVariable Long clientId) {
        List<Job> jobs = jobService.findAllByClientId(clientId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<List<Job>> getMyJobs(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Client client = clientService.findByEmail(new Email(userDetails.getUsername()));
        
        List<Job> jobs = jobService.findAllByClientId(client.getId());
        return ResponseEntity.ok(jobs);
    }
}