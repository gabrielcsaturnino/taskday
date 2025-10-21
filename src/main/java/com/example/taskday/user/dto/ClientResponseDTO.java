package com.example.taskday.user.dto;

import com.example.taskday.auxiliary.Address;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Phone;

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
