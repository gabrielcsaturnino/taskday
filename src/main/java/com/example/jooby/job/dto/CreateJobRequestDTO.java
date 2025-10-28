package com.example.jooby.job.dto;

import com.example.jooby.address.dto.CreateAddressRequestDTO;

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
