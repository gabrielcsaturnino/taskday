package com.example.jooby;

import com.example.jooby.address.dto.CreateAddressRequestDTO;
import com.example.jooby.job.Job;
import com.example.jooby.job.JobApplication;
import com.example.jooby.job.enums.JobApplicationStatusEnum;
import com.example.jooby.job.repository.JobApplicationRepository;
import com.example.jooby.job.repository.JobRepository;
import com.example.jooby.job.service.JobApplicationService;
import com.example.jooby.job.service.JobService;
import com.example.jooby.user.Client;
import com.example.jooby.user.Contractor;
import com.example.jooby.user.dto.CreateClientRequestDTO;
import com.example.jooby.user.dto.CreateContractorRequestDTO;
import com.example.jooby.user.repository.ClientRepository;
import com.example.jooby.user.repository.ContractorRepository;
import com.example.jooby.user.service.ClientService;
import com.example.jooby.user.service.ContractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class JobApplicationIT {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ContractorService contractorService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    private Client testClient;
    private Contractor testContractor;
    private Job testJob;

    @BeforeEach
    void setUp() {
        // Create test client
        CreateAddressRequestDTO clientAddress = new CreateAddressRequestDTO(
                "Client St", "123", "Apt 1", "Downtown", "12345-678", "Test City"
        );
        CreateClientRequestDTO createClientDTO = new CreateClientRequestDTO(
                "Test", "Client", "123456789", "Password123!",
                "(11) 99999-9999", "client@test.com", "11144477735",
                "1990-01-01", clientAddress
        );
        clientService.createClient(createClientDTO);
        testClient = clientRepository.findAll().get(0);

        // Create test contractor
        CreateAddressRequestDTO contractorAddress = new CreateAddressRequestDTO(
                "Contractor St", "456", "Suite 2", "Uptown", "23456-789", "Test City"
        );
        CreateContractorRequestDTO createContractorDTO = new CreateContractorRequestDTO(
                "Test", "Contractor", "987654321", "Password123!",
                "(11) 98888-8888", "contractor@test.com", "08063359127",
                "1990-01-01", "Experienced contractor", contractorAddress
        );
        contractorService.createContractor(createContractorDTO);
        testContractor = contractorRepository.findAll().get(0);

        // Create test job
        CreateAddressRequestDTO jobAddress = new CreateAddressRequestDTO(
                "Job St", "789", "Office 3", "Business", "34567-890", "Test City"
        );
        com.example.jooby.job.dto.CreateJobRequestDTO createJobDTO = new com.example.jooby.job.dto.CreateJobRequestDTO(
                "Test Job", "Test job description", 50, jobAddress
        );
        jobService.createJob(createJobDTO, testClient.getId());
        testJob = jobRepository.findAll().get(0);
    }

    @Test
    void createJobApplication_ShouldCreateApplicationSuccessfully() {
        // When
        JobApplication application = jobApplicationService.createJobApplication(
                testJob.getId(), testContractor.getId()
        );

        // Then
        assertNotNull(application);
        assertEquals(testJob.getId(), application.getJob().getId());
        assertEquals(testContractor.getId(), application.getContractor().getId());
        assertEquals(JobApplicationStatusEnum.SUBMITTED, application.getStatusApplication());
    }

    @Test
    void updateJobApplicationStatus_ShouldUpdateStatusSuccessfully() {
        // Given
        JobApplication application = jobApplicationService.createJobApplication(
                testJob.getId(), testContractor.getId()
        );

        // When
        jobApplicationService.updateStatus(application.getId(), JobApplicationStatusEnum.ACCEPTED);

        // Then
        JobApplication updatedApplication = jobApplicationService.findById(application.getId());
        assertEquals(JobApplicationStatusEnum.ACCEPTED, updatedApplication.getStatusApplication());
    }

    @Test
    void updateJobApplicationStatus_ShouldThrowException_WhenChangingFromAccepted() {
        // Given
        JobApplication application = jobApplicationService.createJobApplication(
                testJob.getId(), testContractor.getId()
        );
        jobApplicationService.updateStatus(application.getId(), JobApplicationStatusEnum.ACCEPTED);

        // When & Then
        assertThrows(com.example.jooby.exception.InvalidStatusException.class, () -> {
            jobApplicationService.updateStatus(application.getId(), JobApplicationStatusEnum.REJECTED);
        });
    }

    @Test
    void findJobApplicationById_ShouldReturnApplication_WhenExists() {
        // Given
        JobApplication application = jobApplicationService.createJobApplication(
                testJob.getId(), testContractor.getId()
        );

        // When
        JobApplication foundApplication = jobApplicationService.findById(application.getId());

        // Then
        assertNotNull(foundApplication);
        assertEquals(application.getId(), foundApplication.getId());
    }

    @Test
    void findJobApplicationById_ShouldThrowException_WhenNotExists() {
        // When & Then
        assertThrows(com.example.jooby.exception.NotFoundException.class, () -> {
            jobApplicationService.findById(999L);
        });
    }
}
