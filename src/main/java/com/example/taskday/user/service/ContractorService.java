package com.example.taskday.user.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskday.address.builder.AddressBuilder;
import com.example.taskday.address.dto.CreateAddressRequestDTO;
import com.example.taskday.address.repository.AddressRepository;
import com.example.taskday.auxiliary.Address;
import com.example.taskday.auxiliary.Cpf;
import com.example.taskday.auxiliary.DateOfBirthday;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Password;
import com.example.taskday.auxiliary.Phone;
import com.example.taskday.auxiliary.Rating;
import com.example.taskday.exception.DuplicateFieldException;
import com.example.taskday.exception.InvalidFormatException;
import com.example.taskday.exception.NotFoundException;
import com.example.taskday.exception.NullValueException;
import com.example.taskday.user.Contractor;
import com.example.taskday.user.builder.ContractorBuilder;
import com.example.taskday.user.dto.CreateContractorRequestDTO;
import com.example.taskday.user.repository.ClientRepository;
import com.example.taskday.user.repository.ContractorRepository;

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
    
    public Contractor findById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Id cannot be null"); 
        }
        return contractorRepository.findById(id).orElseThrow(() -> new NotFoundException("Contractor not found with id: " + id));
    }
    
    public boolean existsById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Id cannot be null"); 
        }
        return contractorRepository.existsById(id);
    }

    public void setAvarageRating(Long id, Rating rating) {
        if (id == null || rating == null) {
            throw new NullValueException("Id or Rating cannot be null");
        }

        Contractor contractor = findById(id);
        rating.setValue(contractor.getAvarageRating(), 0.2);
        contractor.setAvarageRating(rating);
        contractorRepository.save(contractor);

    }

    public List<Contractor> findAllById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new NullValueException("Ids cannot be null or empty");
        }
        return contractorRepository.findAllById(ids);
    }
}

