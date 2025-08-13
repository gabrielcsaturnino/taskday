package com.example.taskday.job.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.example.taskday.address.builder.AddressBuilder;
import com.example.taskday.address.repository.AddressRepository;
import com.example.taskday.auxiliary.Address;
import com.example.taskday.exception.NotFoundException;
import com.example.taskday.job.Job;
import com.example.taskday.job.builder.JobBuilder;
import com.example.taskday.job.dto.CreateJobRequestDTO;
import com.example.taskday.job.repository.JobRepository;
import com.example.taskday.user.Client;
import com.example.taskday.user.repository.ClientRepository;

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


    public void createJob(CreateJobRequestDTO createJobDTO, Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
        Job job = new JobBuilder().fromDTO(createJobDTO, client).build();
        Address address = new AddressBuilder().fromDTO(createJobDTO.createAddressRequestDTO()).withOwner(job).build();
        job.setAddress(address);
        client.setJob(job);
        clientRepository.save(client);
        jobRepository.save(job);
        addressRepository.save(address);
    }

    public Job findById(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new NotFoundException("Job not found"));
    }

    public List<Job> findAllByClientId(Long clientId) {
        return jobRepository.findAllByClientId(clientId);
    }

}
