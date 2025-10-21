package com.example.taskday.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(
    @NotBlank(message = "Current password cannot be blank")
    String currentPassword,
    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, message = "New password must have at least 8 characters")
    String newPassword
) {}
