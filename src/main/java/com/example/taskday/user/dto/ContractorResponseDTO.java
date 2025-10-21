package com.example.taskday.user.dto;

import com.example.taskday.auxiliary.Address;
import com.example.taskday.auxiliary.Rating;

import java.time.LocalDateTime;
import java.util.List;

public record ContractorResponseDTO(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone,
    String cpf,
    String rgDocument,
    String dateOfBirthday,
    String description,
    Rating averageRating,
    List<Address> addresses,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
