package com.example.taskday.domain.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskday.domain.exception.DuplicateFieldException;
import com.example.taskday.domain.exception.NotFoundException;
import com.example.taskday.domain.model.Contractor;
import com.example.taskday.domain.model.auxiliary.Address;
import com.example.taskday.domain.model.auxiliary.Cpf;
import com.example.taskday.domain.model.auxiliary.DateOfBirthday;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.auxiliary.Password;
import com.example.taskday.domain.model.auxiliary.Phone;
import com.example.taskday.domain.model.builders.AddressBuilder;
import com.example.taskday.domain.model.builders.ContractorBuilder;
import com.example.taskday.domain.model.dtos.CreateAddressRequestDTO;
import com.example.taskday.domain.model.dtos.CreateContractorRequestDTO;
import com.example.taskday.domain.repositories.AddressRepository;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.ContractorRepository;

@Service
public class ContractorService {
    
    private final ContractorRepository contractorRepository;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    



    public ContractorService(ContractorRepository contractorRepository, ClientRepository clientRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder) {
        this.contractorRepository = contractorRepository;
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    
    public void createContractor(CreateContractorRequestDTO createContractorDTO) {
        ensureUniqueIdentifiers(new Email(createContractorDTO.email()), new Phone(createContractorDTO.phone()), new Cpf(createContractorDTO.cpf()), createContractorDTO.rgDocument());
        CreateAddressRequestDTO createAddressDTO = createContractorDTO.createAddressRequestDTO();
        Contractor contractor = new ContractorBuilder(passwordEncoder).fromDTO(createContractorDTO).build();
        Address address = new AddressBuilder().fromDTO(createAddressDTO).withOwner(contractor).build();
        contractor.setAddress(address);
        contractorRepository.save(contractor);
        addressRepository.save(address);
    }

    private void ensureUniqueIdentifiers(Email email, Phone phone, Cpf cpf, String rgDocument) {
        boolean emailInUse = contractorRepository.existsByEmail(email) || clientRepository.existsByEmail(email);
        boolean phoneInUse = contractorRepository.existsByPhone(phone) || clientRepository.existsByPhone(phone);
        boolean cpfInUse = contractorRepository.existsByCpf(cpf) || clientRepository.existsByCpf(cpf);
        boolean rgInUse = contractorRepository.existsByrgDoc(rgDocument) || clientRepository.existsByrgDoc(rgDocument);


    if(emailInUse) {
        throw new DuplicateFieldException("Email in use");
    }
    if(phoneInUse) {
        throw new DuplicateFieldException("Phone in use");
    }
    if(cpfInUse) {
        throw new DuplicateFieldException("CPF in use");
    }
    if(rgInUse) {
        throw new DuplicateFieldException("RG in use");
    }
    }

    public Contractor findContractorByEmail(Email email) {
        if(email == null) {
            throw new IllegalArgumentException("Identifier cannot be null"); 
        }
        return contractorRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Contractor not found with identifier: " + email));
    }
}


