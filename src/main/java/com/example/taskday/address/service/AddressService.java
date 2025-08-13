package com.example.taskday.address.service;

import org.springframework.stereotype.Service;

import com.example.taskday.address.builder.AddressBuilder;
import com.example.taskday.address.dto.CreateAddressRequestDTO;
import com.example.taskday.address.repository.AddressRepository;
import com.example.taskday.auxiliary.Address;

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
