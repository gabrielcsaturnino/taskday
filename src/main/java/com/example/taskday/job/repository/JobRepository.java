package com.example.taskday.job.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskday.job.Job;
import com.example.taskday.job.enums.JobStatusEnum;
import com.example.taskday.user.Client;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
   Job save(Job job);
   Optional<Job> findById(Long id);
   List<Job> findAllByClientId(Long id);
   long countByJobStatus(JobStatusEnum status);
}
