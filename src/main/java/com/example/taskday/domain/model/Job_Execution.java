package com.example.taskday.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    
    @Column(name = "status_execution", nullable = false)
    private boolean statusExecution = false;
 
    @Column(name = "avarage_rating_job", nullable = false, columnDefinition = "DECIMAL(2,1)")
    private double avarageRating = 0.0;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;



    public Job_Execution() {}

    public Job_Execution(Job job, Contractor contractor) {
        this.job = job;
        this.contractor = contractor;
        this.status = execution_status_enum.PENDING;
    }

    public void setAvarageRating(double avarageRating) {
        if (avarageRating < 0 || avarageRating > 5) {
            throw new IllegalArgumentException("Average rating must be between 0 and 5");
        }

        double currentRating = this.contractor.getAvarageRating();
        double newRating = (currentRating * 0.8) + (avarageRating * 0.2);


        this.contractor.setAvarageRating(newRating);
        this.avarageRating = avarageRating;
    }



    public void changeStatus(execution_status_enum newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }
        if (this.status == newStatus) {
            return; 
        }
        
        if (this.status == execution_status_enum.COMPLETED) {
            this.statusExecution = true;
            this.status = newStatus;
        }

        if (this.status == execution_status_enum.CANCELLED) {
            this.statusExecution = false;
            this.status = newStatus;
        }

        if (this.status != newStatus) {
            this.status = newStatus;
        }
       

        
    }

    
}
