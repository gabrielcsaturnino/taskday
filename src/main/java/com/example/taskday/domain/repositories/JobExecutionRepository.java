package com.example.taskday.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.taskday.domain.model.Contractor;
import com.example.taskday.domain.model.JobExecution;

public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {
    JobExecution save(JobExecution job_Execution);
    Optional<JobExecution> findById(Long id);
    @Query("SELECT je FROM JobExecution je JOIN je.contractors c WHERE c.id = :contractorId")
    List<JobExecution> findAllByContractorId(@Param("contractorId") Long contractorId);
    @Query("SELECT je FROM JobExecution je JOIN je.contractors c where c.id = :contractorId and je.statusExecution = true")
    List<JobExecution> findAllCompletedByContractorId(@Param("contractorId") Long contractorId);
    boolean existsByJobId(Long jobId);
    @Query(
    value = "SELECT contractor_id FROM job_execution_contractor WHERE job_execution_id = :jobExecutionId", 
    nativeQuery = true)
    List<Long> findAllContractorByExecutionId(Long jobExecutionId);

    JobExecution findByJobId(Long jobId);
}
