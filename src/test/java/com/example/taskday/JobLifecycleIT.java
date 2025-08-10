package com.example.taskday;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskday.address.dto.CreateAddressRequestDTO;
import com.example.taskday.address.repository.AddressRepository;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.chatroom.ChatRoom;
import com.example.taskday.chatroom.service.ChatRoomService;
import com.example.taskday.exception.InvalidStatusException;
import com.example.taskday.job.Job;
import com.example.taskday.job.JobApplication;
import com.example.taskday.job.JobExecution;
import com.example.taskday.job.dto.CreateJobRequestDTO;
import com.example.taskday.job.enums.JobApplicationStatusEnum;
import com.example.taskday.job.enums.JobExecutionStatusEnum;
import com.example.taskday.job.repository.JobApplicationRepository;
import com.example.taskday.job.repository.JobExecutionRepository;
import com.example.taskday.job.repository.JobRepository;
import com.example.taskday.job.service.JobApplicationService;
import com.example.taskday.job.service.JobExecutionService;
import com.example.taskday.job.service.JobService;
import com.example.taskday.user.Client;
import com.example.taskday.user.Contractor;
import com.example.taskday.user.dto.CreateClientRequestDTO;
import com.example.taskday.user.dto.CreateContractorRequestDTO;
import com.example.taskday.user.repository.ClientRepository;
import com.example.taskday.user.repository.ContractorRepository;
import com.example.taskday.user.service.ClientService;
import com.example.taskday.user.service.ContractorService;




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

    @Autowired
    private ChatRoomService chatRoomService;






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
        jobService.createJob(createJobDTO, client);
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
        assert contractorRepository.findById(contractor.getId()).get().getAvarageRating().getValue() == 4.9;
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

    @Test
    @Transactional
    public void changeLeader() {
        Contractor contractor = contractorService.findContractorByEmail(new Email("jhonDoe@gmail.com"));
        Contractor contractor2 = contractorService.findContractorByEmail(new Email("janeSmith@gmail.com"));
        Job job = jobService.findById(1L);
        JobApplication jobApplication = jobApplicationService.createJobApplication(job.getId(), contractor.getId());
        jobApplicationService.updateStatus(jobApplication.getId(), JobApplicationStatusEnum.ACCEPTED);
        ChatRoom chatRoom = chatRoomService.findByJobId(job.getId());
        assert chatRoom.getContractor().getId() == contractor.getId();
        JobApplication jobApplication2 = jobApplicationService.createJobApplication(job.getId(), contractor2.getId());
        jobApplicationService.updateStatus(jobApplication2.getId(), JobApplicationStatusEnum.ACCEPTED);
        assert chatRoom.getContractor().getId() == contractor.getId();
        assertNotEquals(chatRoom.getContractor().getId(), contractor2.getId());
        JobExecution jobExecution = jobExecutionService.findJobExecutionByJobId(jobApplication.getJob().getId());
        assert jobExecution.getContractorLeader().getId() == contractor.getId();
        jobExecutionService.changeLeader(jobExecution.getId(), contractor2.getId());
        assert chatRoom.getContractor().getId() == contractor2.getId();
        assert jobExecution.getContractorLeader().getId() == contractor2.getId();
        assert jobExecutionService.findAllContractorByExecutionId(jobExecution.getId()).size() == 2;
        jobExecutionService.excludeContractor(jobExecution.getId(), contractor2.getId());
        assert jobExecutionService.findAllContractorByExecutionId(jobExecution.getId()).size() == 1;
        assert jobExecution.getContractorLeader().getId() == contractor.getId();
        assert chatRoom.getContractor().getId() == contractor.getId();
        
    }

    
}
