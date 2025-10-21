package com.example.taskday.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateClientRequestDTO(
    @NotBlank(message = "First name cannot be blank")
    String firstName,
    @NotBlank(message = "Last name cannot be blank")
    String lastName,
    @NotBlank(message = "Phone cannot be blank")
    String phone,
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email
) {}
