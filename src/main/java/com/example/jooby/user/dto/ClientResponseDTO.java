package com.example.jooby.user.dto;

import com.example.jooby.auxiliary.Address;
import com.example.jooby.auxiliary.Email;
import com.example.jooby.auxiliary.Phone;

import java.time.LocalDateTime;
import java.util.List;

public record ClientResponseDTO(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone,
    String cpf,
    String rgDocument,
    String dateOfBirthday,
    List<Address> addresses,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
