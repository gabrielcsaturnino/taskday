package com.example.taskday.domain.model;

import com.example.taskday.domain.model.auxiliary.Address;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    private Client client;


    public Job() {
        this.statusJob = job_status_enum.INACTIVE;
    }

    public Job(String title, String description, int pricePerHour, job_status_enum statusJob, Client client) {
        setTitle(title);
        setDescription(description);
        setPricePerHour(pricePerHour);
        setStatusJob(statusJob);
        this.client = client;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        this.address = address;
        address.setJob(this);
    }


    
    public void setStatusJob(job_status_enum status_job){
        if (this.statusJob == status_job) {
            return;   
        }
        this.statusJob = status_job;
    }

    public void setPricePerHour(int pricePerHour) {
        if (pricePerHour < 0) {
            throw new IllegalArgumentException("Price per hour cannot be negative");
        }
        this.pricePerHour = pricePerHour;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    public job_status_enum getStatusJob() {
        return statusJob;
    }
   



}
