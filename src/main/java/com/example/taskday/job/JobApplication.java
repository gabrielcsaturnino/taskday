package com.example.taskday.job;

import com.example.taskday.exception.NullValueException;
import com.example.taskday.job.enums.JobApplicationStatusEnum;
import com.example.taskday.job.enums.ApplicationStatusEnum;
import com.example.taskday.user.Contractor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table(name = "client_job_applications")
public class JobApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_application")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_job")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "id_contractor")
    private Contractor contractor;

    @Column(name = "status_application", nullable = false)
    private JobApplicationStatusEnum statusApplication;

    public JobApplication() {}
    public JobApplication(Job job, Contractor contractor) {
        this.job = job;
        this.contractor = contractor;
        this.statusApplication = JobApplicationStatusEnum.SUBMITTED;
    }
    


    public Long getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public JobApplicationStatusEnum getStatusApplication() {
        return statusApplication;
    }


    public void setStatusApplication(JobApplicationStatusEnum statusApplication) {
        if (statusApplication == null) {
            throw new NullValueException("Status application cannot be null");
        }
        this.statusApplication = statusApplication;
    }

   
}


