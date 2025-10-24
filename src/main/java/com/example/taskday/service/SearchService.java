package com.example.taskday.service;

import com.example.taskday.job.Job;
import com.example.taskday.job.dto.JobSearchDTO;
import com.example.taskday.job.repository.JobRepository;
import com.example.taskday.user.Contractor;
import com.example.taskday.user.dto.ContractorSearchDTO;
import com.example.taskday.user.repository.ContractorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    
    private final JobRepository jobRepository;
    private final ContractorRepository contractorRepository;
    
    public SearchService(JobRepository jobRepository, ContractorRepository contractorRepository) {
        this.jobRepository = jobRepository;
        this.contractorRepository = contractorRepository;
    }
    
    public List<Job> searchJobs(JobSearchDTO searchDTO) {
        // Implementar busca avançada de jobs        
        return jobRepository.findAll();
    }
    
    public List<Contractor> searchContractors(ContractorSearchDTO searchDTO) {
        // Implementar busca avançada de contratantes
        return contractorRepository.findAll();
    }
}
