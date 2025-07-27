package com.example.taskday.domain.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClientRequestDTO(
    
    @NotBlank(message = "First name cannot be blank")
    String firstName,
    @NotBlank(message = "Last name cannot be blank")
    String lastName,
    @NotBlank(message = "RG document cannot be blank")
    String rgDocument,
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must have at least 8 characters")
    String password,
    @NotBlank(message = "Phone cannot be blank")
    String phone,
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email,
    @NotBlank(message = "CPF cannot be blank")
    String cpf,
    @NotBlank(message = "Date of birthday cannot be blank")
    String dateOfBirthday
) {}
