package com.example.taskday.job.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskday.auxiliary.Email;
import com.example.taskday.job.Job;
import com.example.taskday.job.dto.CreateJobRequestDTO;
import com.example.taskday.job.dto.UpdateJobRequestDTO;
import com.example.taskday.job.dto.JobSearchDTO;
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
        
        jobService.createJob(createJobDTO, client.getId());
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

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobRequestDTO updateJobDTO,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Client client = clientService.findByEmail(new Email(userDetails.getUsername()));
        
        // Verificar se o job pertence ao cliente
        Job job = jobService.findById(id);
        if (!job.getClient().getId().equals(client.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Job updatedJob = jobService.updateJob(id, updateJobDTO);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(
            @PathVariable Long id,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Client client = clientService.findByEmail(new Email(userDetails.getUsername()));
        
        // Verificar se o job pertence ao cliente
        Job job = jobService.findById(id);
        if (!job.getClient().getId().equals(client.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully");
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<String> closeJob(
            @PathVariable Long id,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Client client = clientService.findByEmail(new Email(userDetails.getUsername()));
        
        // Verificar se o job pertence ao cliente
        Job job = jobService.findById(id);
        if (!job.getClient().getId().equals(client.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        jobService.closeJob(id);
        return ResponseEntity.ok("Job closed successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String location) {
        
        JobSearchDTO searchDTO = new JobSearchDTO(title, description, minPrice, maxPrice, location, null, 0, 10, "createdAt", "desc");
        List<Job> jobs = jobService.searchJobs(searchDTO);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Job>> getActiveJobs() {
        List<Job> jobs = jobService.findActiveJobs();
        return ResponseEntity.ok(jobs);
    }
}