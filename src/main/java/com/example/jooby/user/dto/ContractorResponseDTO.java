package com.example.jooby.user.dto;

import com.example.jooby.auxiliary.Address;
import com.example.jooby.auxiliary.Rating;

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
