package com.example.taskday.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
   Job save(Job job);
   Optional<Job> findById(Long id);
   List<Job> findAllByClientId(Long id);
}
