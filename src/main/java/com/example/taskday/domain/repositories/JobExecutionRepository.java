package com.example.taskday.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskday.domain.model.JobExecution;

public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {
    JobExecution save(JobExecution job_Execution);
    Optional<JobExecution> findById(Long id);
    List<JobExecution> findAllByJobId(Long jobId);
    List<JobExecution> findAllByContractorId(Long contractorId);
    List<JobExecution> findAllCompletedByContractorId(Long contractorId);
}
