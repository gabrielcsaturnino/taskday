package com.example.taskday.domain.model;

import com.example.taskday.domain.enums.JobApplicationStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


enum application_status_enum {
    submitted,
    rejected,
    accepted
}

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
        this.statusApplication = statusApplication.SUBMITTED;
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
        this.statusApplication = statusApplication;
    }

}


