package com.example.jooby.address.service;

import org.springframework.stereotype.Service;

import com.example.jooby.address.builder.AddressBuilder;
import com.example.jooby.address.dto.CreateAddressRequestDTO;
import com.example.jooby.address.repository.AddressRepository;
import com.example.jooby.auxiliary.Address;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    
    public Address createAddress(CreateAddressRequestDTO createAddressRequestDTO) {
       
        
        Address address = new AddressBuilder().fromDTO(createAddressRequestDTO).build();
        return addressRepository.save(address);
    }

}
