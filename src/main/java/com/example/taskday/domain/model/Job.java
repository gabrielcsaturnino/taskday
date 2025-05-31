package com.example.taskday.domain.model;

import com.example.taskday.domain.model.auxiliary.Address;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


enum job_status_enum {
    ACTIVE,
    INACTIVE
}

@Entity
@Table(name = "client_jobs")
public class Job {
    
    @Id
    @Column(name = "id_job")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "job_salary", nullable = false)
    private int pricePerHour;
    
    @Column(name = "status_job", nullable = false)
    private job_status_enum statusJob;
    
    @OneToOne(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;
}
