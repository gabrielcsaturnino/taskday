package com.example.taskday.domain.model;

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
    viewed,
    shortlisted,
    rejected,
    accepted
}

@Entity
@Table(name = "client_job_applications")
public class Job_Application {
    
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
    private application_status_enum statusApplication;
    
}