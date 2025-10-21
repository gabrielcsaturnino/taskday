package com.example.taskday.job.dto;

import com.example.taskday.auxiliary.Address;
import com.example.taskday.job.enums.JobStatusEnum;
import com.example.taskday.user.Client;

import java.time.LocalDateTime;

public record JobResponseDTO(
    Long id,
    String title,
    String description,
    int pricePerHour,
    JobStatusEnum jobStatus,
    Address address,
    Client client,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
