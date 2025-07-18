package com.example.taskday.domain.model;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.taskday.domain.enums.JobApplicationStatusEnum;
import com.example.taskday.domain.enums.JobExecutionStatusEnum;

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

    @ManyToOne
    @JoinColumn(name = "id_contractor", nullable = false)
    private Contractor contractor;

    @Column(name = "status", nullable = false)
    private JobExecutionStatusEnum status;

    @Column(name = "status_execution", nullable = false)
    private boolean statusExecution = false;

    @Column(name = "avarage_rating_job", nullable = false, columnDefinition = "DECIMAL(2,1)")
    private double avarageRating = 0.0;

    @ManyToOne
    @JoinColumn(name = "id_application", nullable = false)
    private JobApplication jobApplication;

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

    public JobExecution(JobApplication jobApplication) {
        if (jobApplication.getStatusApplication() != JobApplicationStatusEnum.ACCEPTED) {
            throw new IllegalArgumentException("Job application must be accepted to create a job execution.");
        }
        this.job = jobApplication.getJob();
        this.contractor = jobApplication.getContractor();
        this.status = JobExecutionStatusEnum.PENDING;
        this.jobApplication = jobApplication;
    }

    public Long getId() { return id; }
    public Job getJob() { return job; }
    public Contractor getContractor() { return contractor; }
    public JobExecutionStatusEnum getStatus() { return status; }
    public boolean isStatusExecution() { return statusExecution; }
    public double getAvarageRating() { return avarageRating; }
    public JobApplication getJobApplication() { return jobApplication; }
    public LocalDateTime getCreated_at() { return created_at; }
    public LocalDateTime getUpdated_at() { return updated_at; }
    public LocalDateTime getCompleted_at() { return completed_at; }
    public LocalDateTime getIn_progress_at() { return in_progress_at; }
    public BigInteger getTotalTime() { return totalTime; }

    public void setStatus(JobExecutionStatusEnum status) { this.status = status; }
    public void setStatusExecution(boolean statusExecution) { this.statusExecution = statusExecution; }
    public void setAvarageRating(double avarageRating) { this.avarageRating = avarageRating; }
    public void setCompleted_at(LocalDateTime completed_at) { this.completed_at = completed_at; }
    public void setIn_progress_at(LocalDateTime in_progress_at) { this.in_progress_at = in_progress_at; }
    public void setTotalTime(BigInteger totalTime) { this.totalTime = totalTime; }
}