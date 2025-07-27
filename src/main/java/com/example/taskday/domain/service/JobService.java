package com.example.taskday.domain.service;

import org.springframework.stereotype.Service;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.Job;
import com.example.taskday.domain.model.builders.JobBuilder;
import com.example.taskday.domain.model.dtos.CreateJobRequestDTO;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.JobRepository;

@Service
public class JobService {

    ClientRepository clientRepository;
    JobRepository jobRepository;

    public JobService(ClientRepository clientRepository, JobRepository jobRepository) {
    this.clientRepository = clientRepository;
    this.jobRepository = jobRepository;
    }


    public void CreateJob(CreateJobRequestDTO createJobDTO, Client client) {
        Job job = new JobBuilder().fromDTO(createJobDTO, client).build();
        client.setJob(job);
        clientRepository.save(client);
        jobRepository.save(job);
    }
}
