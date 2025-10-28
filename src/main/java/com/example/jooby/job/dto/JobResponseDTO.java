package com.example.jooby.job.dto;

import com.example.jooby.auxiliary.Address;
import com.example.jooby.job.enums.JobStatusEnum;
import com.example.jooby.user.Client;

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
