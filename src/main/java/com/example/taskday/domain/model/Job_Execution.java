package com.example.taskday.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

enum execution_status_enum {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

@Entity
@Table(name = "client_job_execution")
public class Job_Execution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_execution")
    private Long id;

    @JoinColumn(name = "id_job", nullable = false)
    @ManyToOne
    private Job job;

    @JoinColumn(name = "id_contractor", nullable = false)
    @ManyToOne
    private Contractor contractor;

    @Column(name = "status", nullable = false)
    private execution_status_enum status;


    public Job_Execution() {}

    
}
