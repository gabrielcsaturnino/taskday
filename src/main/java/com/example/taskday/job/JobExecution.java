package com.example.taskday.job;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.taskday.exception.NullValueException;
import com.example.taskday.job.enums.JobApplicationStatusEnum;
import com.example.taskday.job.enums.JobExecutionStatusEnum;
import com.example.taskday.user.Contractor;

import jakarta.persistence.*;

@Entity
@Table(name = "client_job_execution")
public class JobExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_execution")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_job", nullable = false)
    private Job job;

    @ManyToMany
    @JoinTable(
        name = "job_execution_contractor", // Nome da tabela de ligação
        joinColumns = @JoinColumn(name = "job_execution_id"), // Chave desta entidade
        inverseJoinColumns = @JoinColumn(name = "contractor_id") // Chave da outra entidade
    )
    private List<Contractor> contractors;
    
    @ManyToOne
    @JoinColumn(name = "id_contractor_leader", nullable = false)
    private Contractor contractorLeader;


    @Column(name = "status", nullable = false)
    private JobExecutionStatusEnum status;

    @Column(name = "status_execution", nullable = false)
    private boolean statusExecution = false;

    @Column(name = "avarage_rating_job", nullable = false, columnDefinition = "DECIMAL(2,1)")
    private double rating = 0.0;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    @Column(name = "completed_at", nullable = true, updatable = false)
    private LocalDateTime completed_at;

    @Column(name = "in_progress_at", nullable = true, updatable = false)
    private LocalDateTime in_progress_at;

    @Column(name = "total_time", nullable = true, updatable = false)
    private BigInteger totalTime;

    public JobExecution() {}

    public JobExecution(List<Contractor> contractors, Job job) {
        this.job = job;
        this.contractors = contractors;
        this.contractorLeader = contractors.get(0);
        // LIST DE CONTRACTOR (UM JOB EXECUTION PODE CONTER VARIOS TRABALHADORES)
        this.status = JobExecutionStatusEnum.PENDING;
    }

    public Long getId() { return id; }
    public Job getJob() { return job; }
    public List<Contractor> getContractor() { return contractors; }
    public JobExecutionStatusEnum getStatus() { return status; }
    public boolean isStatusExecution() { return statusExecution; }
    public double getRating() { return rating; }
    public LocalDateTime getCreated_at() { return created_at; }
    public LocalDateTime getUpdated_at() { return updated_at; }
    public LocalDateTime getCompletedAt() { return completed_at; }
    public LocalDateTime getInProgressAt() { return in_progress_at; }
    public BigInteger getTotalTime() { return totalTime; }

    public void setStatus(JobExecutionStatusEnum status) {
        if (status == null) {
            throw new NullValueException("New status cannot be null");
        }
        this.status = status;
    }
    public void setStatusExecution(boolean statusExecution) { this.statusExecution = statusExecution; }
    public void setRating(double rating) {this.rating = rating;}
    public void setCompletedAt(LocalDateTime completed_at) { this.completed_at = completed_at; }
    public void setInProgressAt(LocalDateTime in_progress_at) { this.in_progress_at = in_progress_at; }
    public void setTotalTime(BigInteger totalTime) { this.totalTime = totalTime; }
    public void setContractorLeader(Contractor contractorLeader) {this.contractorLeader = contractorLeader; }
    public Contractor getContractorLeader() {return contractorLeader;}
}