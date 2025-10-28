package com.example.jooby.job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateJobRequestDTO(
    @NotBlank(message = "Title cannot be blank")
    String title,
    @NotBlank(message = "Description cannot be blank")
    String description,
    @NotNull(message = "Price per hour cannot be null")
    @Positive(message = "Price per hour must be positive")
    Integer pricePerHour
) {}
