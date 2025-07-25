package com.example.taskday;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
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
import com.example.taskday.domain.model.auxiliary.Address;
import com.example.taskday.domain.model.auxiliary.Cpf;
import com.example.taskday.domain.model.auxiliary.DateOfBirthday;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.auxiliary.Password;
import com.example.taskday.domain.model.auxiliary.Phone;
import com.example.taskday.domain.repositories.AddressRepository;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.ContractorRepository;
import com.example.taskday.domain.repositories.JobApplicationRepository;
import com.example.taskday.domain.repositories.JobExecutionRepository;
import com.example.taskday.domain.repositories.JobRepository;
import com.example.taskday.domain.service.JobApplicationService;
import com.example.taskday.domain.service.JobExecutionService;



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
    private AddressRepository addressRepository;

    @Autowired
    private JobExecutionService jobExecutionService;

    @Autowired
    private JobApplicationService jobApplicationService;




    @BeforeEach
    public void setUp() {
        
        Contractor contractor = new Contractor("John", "Doe", "123456789", 
                new Password("securePassword123#"), true, 
                new Phone("62983374358"), new Email("jhonDoe@gmail.com"),
                new Cpf("08063359127"), new DateOfBirthday(LocalDate.of(1990, 1, 1)));
        
        Address address = new Address("Main St", "123", "Apt 456", "Downtown", "Cityville", "12345-678", contractor);
        contractor.setAddress(address);
        contractorRepository.save(contractor);
        addressRepository.save(address);

        Client client = new Client("Jane", "Smith", new Phone("62998765432"), 
                new Email("clientEmail@gmail.com"),
                new Cpf("79109910026"),
                new Password("clientPassword123#"), true, "987654321",
                new DateOfBirthday(LocalDate.of(1992, 2, 2)));
        Address address1 = new Address("Second St", "456", "Suite 789", "Uptown", "Townsville", "23456-789", client);        
        client.setAddress(address1);
        clientRepository.save(client);
        addressRepository.save(address1);

        Job job = new Job("Software Development", "Develop a new feature", 140,  client);
        jobRepository.save(job);
        client.setJob(job);
        clientRepository.save(client);
    }

    @Test
    @Transactional
    public void completedJobExecution(){
        
        Optional<Contractor> contractor = contractorRepository.findByEmail(new Email("jhonDoe@gmail.com"));
        List<Job> jobs = jobRepository.findAll();
        JobApplication jobApplication = jobApplicationService.createJobApplication(jobs.get(0).getId(), contractor.get().getId());
        
        assert jobApplicationRepository.findAllByContractorId(contractor.get().getId()).get(0).getStatusApplication() == JobApplicationStatusEnum.SUBMITTED; 
        Exception ex = assertThrows(InvalidStatusException.class, () -> jobExecutionService.createJobExecution(jobApplication.getId()));
        assert ex.getMessage().equals("Job application must be accepted to create a job execution.");
        
        jobApplicationService.updateStatus(jobApplication.getId() ,JobApplicationStatusEnum.ACCEPTED);
        JobExecution jobExecution = jobExecutionService.createJobExecution(jobApplication.getId());
        assert jobExecutionRepository.findAllByContractorId(contractor.get().getId()).get(0).getStatus() == JobExecutionStatusEnum.PENDING;

        jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.IN_PROGRESS);

        assert jobExecutionRepository.findAllByContractorId(contractor.get().getId()).get(0).getIn_progress_at() != null;
        Exception ex2 = assertThrows(InvalidStatusException.class, () -> jobExecutionService.updateAvarageRating(jobExecution.getId(), 4.5));
        assert ex2.getMessage().equals("Job execution must be completed to update the average rating.");
        jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.COMPLETED);
        jobExecutionService.updateAvarageRating(jobExecution.getId(), 4.5);
        contractorRepository.save(contractor.get());
        assert jobExecutionRepository.findAllContractorByExecutionId(jobExecution.getId()).get(0) == jobExecution.getContractorLeader().getId();
        assert contractorRepository.findById(contractor.get().getId()).get().getAvarageRating() == 4.9;
        System.out.println("id do contractor:" + contractor.get().getId());
        List<JobExecution> jobExecutions = jobExecutionRepository.findAllCompletedByContractorId(contractor.get().getId());
        assert jobExecutions.size() == 1;

    }


    @Test
    @Transactional
    public void cancelledJobExecution() {
        Optional<Contractor> contractor = contractorRepository.findByEmail(new Email("jhonDoe@gmail.com"));
        List<Job> jobs = jobRepository.findAll();
        JobApplication jobApplication = jobApplicationService.createJobApplication(jobs.get(0).getId(), contractor.get().getId());

        jobApplicationService.updateStatus(jobApplication.getId(), JobApplicationStatusEnum.ACCEPTED);


        JobExecution jobExecution = jobExecutionService.createJobExecution(jobApplication.getId());

        assert jobExecutionRepository.findAllByContractorId(contractor.get().getId()).get(0).getStatus() == JobExecutionStatusEnum.PENDING;
        jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.CANCELLED);
        assert jobExecutionRepository.findAllByContractorId(contractor.get().getId()).get(0).getStatus() == JobExecutionStatusEnum.CANCELLED;
        Exception ex = assertThrows(InvalidStatusException.class,  () -> jobExecutionService.updateAvarageRating(jobExecution.getId(), 0));
        assert ex.getMessage().equals("Job execution must be completed to update the average rating.");
        Exception ex2 = assertThrows(InvalidStatusException.class, () -> jobExecutionService.updateStatus(jobExecution.getId(), JobExecutionStatusEnum.IN_PROGRESS));
        assert ex2.getMessage().equals("Cannot change status from CANCELLED to another status.");
    }

    
}
