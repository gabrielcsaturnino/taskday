package com.example.jooby.job.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jooby.job.JobApplication;
import com.example.jooby.job.enums.ApplicationStatusEnum;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    JobApplication save(JobApplication jobApplication);
    Optional<JobApplication> findById(Long id);
    List<JobApplication> findAllByJobId(Long jobId);
    List<JobApplication> findAllByContractorId(Long contractorId);
    long countByStatusApplication(ApplicationStatusEnum status);
    
}
