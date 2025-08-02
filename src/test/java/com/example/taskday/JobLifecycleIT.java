package com.example.taskday;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskday.domain.enums.JobApplicationStatusEnum;
import com.example.taskday.domain.enums.JobExecutionStatusEnum;
import com.example.taskday.domain.exception.InvalidStatusException;
import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.Contractor;
import com.example.taskday.domain.model.Job;
import com.example.taskday.domain.model.JobApplication;
import com.example.taskday.domain.model.JobExecution;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.dtos.CreateAddressRequestDTO;
import com.example.taskday.domain.model.dtos.CreateClientRequestDTO;
import com.example.taskday.domain.model.dtos.CreateContractorRequestDTO;
import com.example.taskday.domain.model.dtos.CreateJobRequestDTO;
import com.example.taskday.domain.repositories.AddressRepository;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.ContractorRepository;
import com.example.taskday.domain.repositories.JobApplicationRepository;
import com.example.taskday.domain.repositories.JobExecutionRepository;
import com.example.taskday.domain.repositories.JobRepository;
import com.example.taskday.domain.service.ContractorService;
import com.example.taskday.domain.service.ClientService;
import com.example.taskday.domain.service.JobApplicationService;
import com.example.taskday.domain.service.JobExecutionService;
import com.example.taskday.domain.service.JobService;




@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class JobLifecycleIT {
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobExecutionRepository jobExecutionRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobExecutionService jobExecutionService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private ContractorService contractorService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private JobService jobService;






    @BeforeEach
    public void setUp() {
        CreateAddressRequestDTO createAddressDTO = new CreateAddressRequestDTO("Main St", "123", "Apt 456", "Downtown", "12345-678","Cityville");
        CreateContractorRequestDTO createContractorDTO = new CreateContractorRequestDTO ("John", "Doe", "123456789", 
                "securePassword123#",  
                "62983374358","jhonDoe@gmail.com",
                "08063359127","1990-01-01", createAddressDTO);
        contractorService.createContractor(createContractorDTO);

        CreateAddressRequestDTO createAddressDTO2 = new CreateAddressRequestDTO("Second St", "456", "Suite 789", "Uptown", "23456-789","Townsville");
        CreateContractorRequestDTO createContractorDTO2 = new CreateContractorRequestDTO("Jane", "Smith", "987654921", 
                "securePassword456#",  
                "62996109984", "janeSmith@gmail.com",
                "79109910026", "1990-01-01", createAddressDTO2);
        contractorService.createContractor(createContractorDTO2);


        CreateAddressRequestDTO createAddressDTO3 = new CreateAddressRequestDTO("Second St", "456", "Suite 789", "Uptown", "23456-789","Townsville");
        CreateClientRequestDTO createClientDTO = new CreateClientRequestDTO("Jane", "Smith", "987654329", "clientPassword123#", "62998765432", 
                "clientEmail@gmail.com",
                "02920643223",
                "1990-01-01", createAddressDTO3);
        clientService.createClient(createClientDTO);

        Client client = clientRepository.findByEmail(new Email("clientEmail@gmail.com")).get();
        
        CreateJobRequestDTO createJobDTO = new CreateJobRequestDTO("Software Development", "Develop a new feature", 140, createAddressDTO3);
        jobService.CreateJob(createJobDTO, client);
    }

    @Test
    @Transactional
    public void completedJobExecution(){
        
        Contractor contractor = contractorService.findContractorByEmail(new Email("jhonDoe@gmail.com"));
        List<Job> jobs = jobRepository.findAll();
        JobApplication jobApplication = jobApplicationService.createJobApplication(jobs.get(0).getId(), contractor.getId());
        
        assert jobApplicationRepository.findAllByContractorId(contractor.getId()).get(0).getStatusApplication() == JobApplicationStatusEnum.SUBMITTED; 
        Exception ex = assertThrows(InvalidStatusException.class, () -> jobExecutionService.createJobExecution(jobApplication.getId()));
        assert ex.getMessage().equals("Job application must be accepted to create a job execution.");
        
        jobApplicationService.updateStatus(jobApplication.getId() ,JobApplicationStatusEnum.ACCEPTED);
        JobExecution jobExecution = jobExecutionRepository.findByJobId(jobApplication.getJob().getId());
        assert jobExecutionRepository.findAllByContractorId(contractor.getId()).get(0).getStatus() == JobExecutionStatusEnum.PENDING;

        jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.IN_PROGRESS);

        assert jobExecutionRepository.findAllByContractorId(contractor.getId()).get(0).getIn_progress_at() != null;
        Exception ex2 = assertThrows(InvalidStatusException.class, () -> jobExecutionService.updateAvarageRating(jobExecution.getId(), 4.5));
        assert ex2.getMessage().equals("Job execution must be completed to update the average rating.");
        jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.COMPLETED);
        jobExecutionService.updateAvarageRating(jobExecution.getId(), 4.5);
        contractorRepository.save(contractor);
        assert jobExecutionRepository.findAllContractorByExecutionId(jobExecution.getId()).get(0) == jobExecution.getContractorLeader().getId();
        assert contractorRepository.findById(contractor.getId()).get().getAvarageRating() == 4.9;
        System.out.println("id do contractor:" + contractor.getId());
        List<JobExecution> jobExecutions = jobExecutionRepository.findAllCompletedByContractorId(contractor.getId());
        assert jobExecutions.size() == 1;

    }


    @Test
    @Transactional
    public void cancelledJobExecution() {
        Optional<Contractor> contractor = contractorRepository.findByEmail(new Email("jhonDoe@gmail.com"));
        List<Job> jobs = jobRepository.findAll();
        JobApplication jobApplication = jobApplicationService.createJobApplication(jobs.get(0).getId(), contractor.get().getId());

        jobApplicationService.updateStatus(jobApplication.getId(), JobApplicationStatusEnum.ACCEPTED);


        assert jobExecutionRepository.findAllByContractorId(contractor.get().getId()).get(0).getStatus() == JobExecutionStatusEnum.PENDING;
        JobExecution jobExecution = jobExecutionRepository.findByJobId(jobApplication.getJob().getId());
        jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.CANCELLED);
        assert jobExecutionRepository.findAllByContractorId(contractor.get().getId()).get(0).getStatus() == JobExecutionStatusEnum.CANCELLED;
        Exception ex = assertThrows(InvalidStatusException.class,  () -> jobExecutionService.updateAvarageRating(jobExecution.getId(), 0));
        assert ex.getMessage().equals("Job execution must be completed to update the average rating.");
        Exception ex2 = assertThrows(InvalidStatusException.class, () -> jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.IN_PROGRESS));
        assert ex2.getMessage().equals("Cannot change status from CANCELLED to another status.");
    }

    @Test
    @Transactional
    public void addNewContractor() {
        Contractor contractor1 = contractorRepository.findByEmail(new Email("jhonDoe@gmail.com")).get();
        Contractor contractor2 = contractorRepository.findByEmail(new Email("janeSmith@gmail.com")).get();
        Job job = jobRepository.findAll().get(0);
        assert job.getTitle().equals("Software Development");

        JobApplication jobApplication1 = jobApplicationService.createJobApplication(job.getId(), contractor1.getId());
        JobApplication jobApplication2 = jobApplicationService.createJobApplication(job.getId(), contractor2.getId());

        jobApplicationService.updateStatus(jobApplication1.getId(), JobApplicationStatusEnum.ACCEPTED);
        jobApplicationService.updateStatus(jobApplication2.getId(), JobApplicationStatusEnum.ACCEPTED);
        JobExecution jobExecution = jobExecutionService.findJobExecutionByJobId(jobApplication1.getJob().getId());
        assert jobExecutionService.findAllContractorByExecutionId(jobExecution.getId()).size() == 2;
        assert jobExecution.getContractorLeader().getId() == contractor1.getId();
        jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.IN_PROGRESS);
        jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.COMPLETED);
        jobExecutionService.updateAvarageRating(jobExecution.getId(), 5);
        assert jobExecutionService.findAllCompletedByContractorId(contractor1.getId()).size() == 1;
        assert jobExecutionService.findAllCompletedByContractorId(contractor2.getId()).size() == 1;
        assert jobExecutionService.findJobExecutionByJobId(jobApplication1.getJob().getId()).getContractor().get(0).getEmail().equals("jhonDoe@gmail.com");
        assert jobExecutionService.findJobExecutionByJobId(jobApplication1.getJob().getId()).getContractor().get(1).getEmail().equals("janeSmith@gmail.com");

       
    }

    
}
