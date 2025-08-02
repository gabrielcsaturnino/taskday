package com.example.taskday.domain.service;

import org.springframework.stereotype.Service;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.Job;
import com.example.taskday.domain.model.auxiliary.Address;
import com.example.taskday.domain.model.builders.AddressBuilder;
import com.example.taskday.domain.model.builders.JobBuilder;
import com.example.taskday.domain.model.dtos.CreateJobRequestDTO;
import com.example.taskday.domain.repositories.AddressRepository;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.JobRepository;

@Service
public class JobService {

    ClientRepository clientRepository;
    JobRepository jobRepository;
    AddressRepository addressRepository;

    public JobService(ClientRepository clientRepository, JobRepository jobRepository, AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
    this.clientRepository = clientRepository;
    this.jobRepository = jobRepository;
    }


    public void CreateJob(CreateJobRequestDTO createJobDTO, Client client) {
        Job job = new JobBuilder().fromDTO(createJobDTO, client).build();
        Address address = new AddressBuilder().fromDTO(createJobDTO.createAddressRequestDTO()).withOwner(job).build();
        job.setAddress(address);
        client.setJob(job);
        clientRepository.save(client);
        jobRepository.save(job);
        addressRepository.save(address);
    }
}
