package com.example.taskday.controller;

import com.example.taskday.job.repository.JobRepository;
import com.example.taskday.job.repository.JobApplicationRepository;
import com.example.taskday.user.repository.ClientRepository;
import com.example.taskday.user.repository.ContractorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/metrics")
public class MetricsController {
    
    private final ClientRepository clientRepository;
    private final ContractorRepository contractorRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    
    public MetricsController(ClientRepository clientRepository, 
                           ContractorRepository contractorRepository,
                           JobRepository jobRepository,
                           JobApplicationRepository jobApplicationRepository) {
        this.clientRepository = clientRepository;
        this.contractorRepository = contractorRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Contadores básicos
        metrics.put("totalClients", clientRepository.count());
        metrics.put("totalContractors", contractorRepository.count());
        metrics.put("totalJobs", jobRepository.count());
        metrics.put("totalApplications", jobApplicationRepository.count());
        
        // Métricas de aplicações por status
        Map<String, Long> applicationStats = new HashMap<>();
        applicationStats.put("submitted", jobApplicationRepository.countByStatusApplication(com.example.taskday.job.enums.ApplicationStatusEnum.SUBMITTED));
        applicationStats.put("accepted", jobApplicationRepository.countByStatusApplication(com.example.taskday.job.enums.ApplicationStatusEnum.ACCEPTED));
        applicationStats.put("rejected", jobApplicationRepository.countByStatusApplication(com.example.taskday.job.enums.ApplicationStatusEnum.REJECTED));
        metrics.put("applicationStats", applicationStats);
        
        return ResponseEntity.ok(metrics);
    }
    
    @GetMapping("/jobs/active")
    public ResponseEntity<Map<String, Object>> getActiveJobsMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        long activeJobs = jobRepository.countByJobStatus(com.example.taskday.job.enums.JobStatusEnum.active);
        long inactiveJobs = jobRepository.countByJobStatus(com.example.taskday.job.enums.JobStatusEnum.inactive);
        
        metrics.put("activeJobs", activeJobs);
        metrics.put("inactiveJobs", inactiveJobs);
        metrics.put("totalJobs", activeJobs + inactiveJobs);
        
        return ResponseEntity.ok(metrics);
    }
}
