package com.example.taskday.domain.model.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressRequestDTO(
    
    @NotBlank(message = "Street cannot be blank")
    String street,
    @NotBlank(message = "Number cannot be blank")
    String number,
    @NotBlank(message = "Neighborhood cannot be blank")
    String neighborhood,
    @NotBlank(message = "City cannot be blank")
    String city,
    @NotBlank(message = "Zip code cannot be blank")
    String zipCode,
    @NotBlank(message = "State cannot be blank")
    String state
) {

}
