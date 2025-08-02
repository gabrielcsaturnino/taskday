package com.example.taskday.domain.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateJobRequestDTO(
    @NotBlank(message = "Title cannot be blank")
    String title,
    @NotBlank(message = "Description cannot be blank")
    String description, 
    @NotEmpty(message = "Price per hour cannot be empty")
    int pricePerHour,
    CreateAddressRequestDTO createAddressRequestDTO
) {}
