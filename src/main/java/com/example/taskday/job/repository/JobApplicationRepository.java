package com.example.taskday.job.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskday.job.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    JobApplication save(JobApplication jobApplication);
    Optional<JobApplication> findById(Long id);
    List<JobApplication> findAllByJobId(Long jobId);
    List<JobApplication> findAllByContractorId(Long contractorId);
    
}
