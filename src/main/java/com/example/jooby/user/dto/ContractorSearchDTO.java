package com.example.jooby.user.dto;

public record ContractorSearchDTO(
    String name,
    String location,
    Double minRating,
    Double maxRating,
    String skill,
    Integer page,
    Integer size,
    String sortBy,
    String sortDirection
) {}
