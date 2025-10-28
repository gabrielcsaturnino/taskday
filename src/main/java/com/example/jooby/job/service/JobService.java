package com.example.jooby.job.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.example.jooby.address.builder.AddressBuilder;
import com.example.jooby.address.repository.AddressRepository;
import com.example.jooby.auxiliary.Address;
import com.example.jooby.exception.NotFoundException;
import com.example.jooby.job.Job;
import com.example.jooby.job.builder.JobBuilder;
import com.example.jooby.job.dto.CreateJobRequestDTO;
import com.example.jooby.job.dto.UpdateJobRequestDTO;
import com.example.jooby.job.dto.JobSearchDTO;
import com.example.jooby.job.enums.JobStatusEnum;
import com.example.jooby.job.repository.JobRepository;
import com.example.jooby.user.Client;
import com.example.jooby.user.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Locale;

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

    public Job updateJob(Long id, UpdateJobRequestDTO updateJobDTO) {
        Job job = findById(id);
        
        job.setTitle(updateJobDTO.title());
        job.setDescription(updateJobDTO.description());
        job.setPricePerHour(updateJobDTO.pricePerHour());
        
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        Job job = findById(id);
        job.setJobStatus(JobStatusEnum.inactive);
        jobRepository.save(job);
    }

    public void closeJob(Long id) {
        Job job = findById(id);
        job.setJobStatus(JobStatusEnum.inactive);
        jobRepository.save(job);
    }

    public List<Job> searchJobs(JobSearchDTO searchDTO) {
        // Implementar busca com filtros
        List<Job> allJobs = jobRepository.findAll();
        
        // Filtrar por título
        if (searchDTO.title() != null && !searchDTO.title().isEmpty()) {
            allJobs = allJobs.stream()
                .filter(job -> job.getTitle().toLowerCase(Locale.US).contains(searchDTO.title().toLowerCase(Locale.US)))
                .toList();
        }
        
        // Filtrar por descrição
        if (searchDTO.description() != null && !searchDTO.description().isEmpty()) {
            allJobs = allJobs.stream()
                .filter(job -> job.getDescription().toLowerCase(Locale.US).contains(searchDTO.description().toLowerCase(Locale.US)))
                .toList();
        }
        
        // Filtrar por preço mínimo
        if (searchDTO.minPrice() != null) {
            allJobs = allJobs.stream()
                .filter(job -> job.getPricePerHour() >= searchDTO.minPrice())
                .toList();
        }
        
        // Filtrar por preço máximo
        if (searchDTO.maxPrice() != null) {
            allJobs = allJobs.stream()
                .filter(job -> job.getPricePerHour() <= searchDTO.maxPrice())
                .toList();
        }
        
        return allJobs;
    }

    public List<Job> findJobsByLocation(String location) {
        return jobRepository.findAll().stream()
            .filter(job -> job.getAddress() != null && 
                job.getAddress().getCity().toLowerCase(Locale.US).contains(location.toLowerCase(Locale.US)))
            .toList();
    }

    public List<Job> findJobsByPriceRange(Integer minPrice, Integer maxPrice) {
        return jobRepository.findAll().stream()
            .filter(job -> {
                if (minPrice != null && job.getPricePerHour() < minPrice) return false;
                if (maxPrice != null && job.getPricePerHour() > maxPrice) return false;
                return true;
            })
            .toList();
    }

    public List<Job> findActiveJobs() {
        return jobRepository.findAll().stream()
            .filter(job -> job.getJobStatus() == JobStatusEnum.active)
            .toList();
    }

}
