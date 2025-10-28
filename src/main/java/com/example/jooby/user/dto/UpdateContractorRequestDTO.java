package com.example.jooby.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateContractorRequestDTO(
    @NotBlank(message = "First name cannot be blank")
    String firstName,
    @NotBlank(message = "Last name cannot be blank")
    String lastName,
    @NotBlank(message = "Phone cannot be blank")
    String phone,
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email,
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description
) {}
