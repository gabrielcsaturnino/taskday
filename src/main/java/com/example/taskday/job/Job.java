package com.example.taskday.job;

import com.example.taskday.auxiliary.Address;
import com.example.taskday.exception.InvalidFormatException;
import com.example.taskday.exception.NullValueException;
import com.example.taskday.job.enums.JobStatusEnum;
import com.example.taskday.user.Client;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;



@Entity
@Table(name = "client_jobs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job {
    
    @Id
    @Column(name = "id_job")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_title", nullable = false, length = 100)
    private String title;

    @Column(name = "job_description", nullable = false)
    private String description;

    @Column(name = "job_salary", nullable = false)
    private int pricePerHour;
    
    @Column(name = "job_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatusEnum jobStatus;
    
    @OneToOne(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", referencedColumnName = "id_client")
    private Client client;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public Job() {
    }

    public Job(String title, String description, int pricePerHour, Client client) {
        this.title = title;
        this.description = description;
        this.pricePerHour = pricePerHour;
        this.jobStatus = JobStatusEnum.active;
        this.client = client;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new NullValueException("Address cannot be null");
        }
        this.address = address;
        address.setJob(this);
    }


    
    public void setJobStatus(JobStatusEnum jobStatus){
        if (this.jobStatus == jobStatus) {
            return;   
        }
        this.jobStatus = jobStatus;
    }

    public void setPricePerHour(int pricePerHour) {
        if (pricePerHour < 0) {
            throw new InvalidFormatException("Price per hour cannot be negative");
        }
        this.pricePerHour = pricePerHour;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new NullValueException("Description cannot be null or empty");
        }
        this.description = description;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new NullValueException("Title cannot be null or empty");
        }
        this.title = title;
    }

    public JobStatusEnum getJobStatus() {
        return jobStatus;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getDescription() {
        return description;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public Address getAddress() {
        return address;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
