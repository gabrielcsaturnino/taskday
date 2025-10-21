package com.example.taskday.job.dto;

import com.example.taskday.job.enums.JobStatusEnum;

public record JobSearchDTO(
    String title,
    String description,
    Integer minPrice,
    Integer maxPrice,
    String location,
    JobStatusEnum status,
    Integer page,
    Integer size,
    String sortBy,
    String sortDirection
) {}
