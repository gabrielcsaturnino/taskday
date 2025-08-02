package com.example.taskday.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskday.domain.exception.DuplicateFieldException;
import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.auxiliary.Address;
import com.example.taskday.domain.model.auxiliary.Cpf;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.auxiliary.Phone;
import com.example.taskday.domain.model.builders.AddressBuilder;
import com.example.taskday.domain.model.builders.ClientBuilder;
import com.example.taskday.domain.model.dtos.CreateAddressRequestDTO;
import com.example.taskday.domain.model.dtos.CreateClientRequestDTO;
import com.example.taskday.domain.repositories.AddressRepository;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.ContractorRepository;

@Service
public class ClientService {
    
    ClientRepository clientRepository;
    ContractorRepository contractorRepository;
    AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    

    public ClientService(ClientRepository clientRepository, ContractorRepository contractorRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.contractorRepository = contractorRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }
        

    public void createClient(CreateClientRequestDTO createClientDTO){
        ensureUniqueIdentifiers(new Email(createClientDTO.email()), new Phone(createClientDTO.phone()), new Cpf(createClientDTO.cpf()), createClientDTO.rgDocument());
        CreateAddressRequestDTO createAddressDTO = createClientDTO.createAddressRequestDTO();
        Client client = new ClientBuilder(passwordEncoder).fromDTO(createClientDTO).build();
        Address address = new AddressBuilder().fromDTO(createAddressDTO).withOwner(client).build();
        client.setAddress(address);
        clientRepository.save(client);
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
}


