package com.example.jooby.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jooby.address.builder.AddressBuilder;
import com.example.jooby.address.dto.CreateAddressRequestDTO;
import com.example.jooby.address.repository.AddressRepository;
import com.example.jooby.auxiliary.Address;
import com.example.jooby.auxiliary.Cpf;
import com.example.jooby.auxiliary.Email;
import com.example.jooby.auxiliary.Password;
import com.example.jooby.auxiliary.Phone;
import com.example.jooby.exception.DuplicateFieldException;
import com.example.jooby.exception.NotFoundException;
import com.example.jooby.user.Client;
import com.example.jooby.user.builder.ClientBuilder;
import com.example.jooby.user.dto.CreateClientRequestDTO;
import com.example.jooby.user.dto.UpdateClientRequestDTO;
import com.example.jooby.user.dto.ChangePasswordRequestDTO;
import com.example.jooby.user.repository.ClientRepository;
import com.example.jooby.user.repository.ContractorRepository;

@Service
public class ClientService {
    
    private final ClientRepository clientRepository;
    private final ContractorRepository contractorRepository;
    private final AddressRepository addressRepository;
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

    public Client findByEmail(Email email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found with email: " + email));
    }
    
    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
    }

    public Client updateClient(Long id, UpdateClientRequestDTO updateClientDTO) {
        Client client = findById(id);
        
        // Verificar se email já está em uso por outro usuário
        if (!client.getEmailObject().getEmail().equals(updateClientDTO.email())) {
            ensureUniqueEmail(new Email(updateClientDTO.email()));
        }
        
        // Verificar se telefone já está em uso por outro usuário
        if (!client.getPhone().getPhoneNumber().equals(updateClientDTO.phone())) {
            ensureUniquePhone(new Phone(updateClientDTO.phone()));
        }
        
        client.setFirstName(updateClientDTO.firstName());
        client.setLastName(updateClientDTO.lastName());
        client.setPhone(new Phone(updateClientDTO.phone()));
        client.setEmail(new Email(updateClientDTO.email()));
        
        return clientRepository.save(client);
    }

    public void changePassword(Long id, ChangePasswordRequestDTO changePasswordDTO) {
        Client client = findById(id);
        
        // Verificar se a senha atual está correta
        if (!passwordEncoder.matches(changePasswordDTO.currentPassword(), client.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        client.setPassword(Password.create(changePasswordDTO.newPassword(), passwordEncoder));
        clientRepository.save(client);
    }

    public void deactivateAccount(Long id) {
        Client client = findById(id);
        client.setStatusAccount(false);
        clientRepository.save(client);
    }

    public void activateAccount(Long id) {
        Client client = findById(id);
        client.setStatusAccount(true);
        clientRepository.save(client);
    }

    private void ensureUniqueEmail(Email email) {
        boolean emailInUse = contractorRepository.existsByEmail(email) || clientRepository.existsByEmail(email);
        if (emailInUse) {
            throw new DuplicateFieldException("Email already in use");
        }
    }

    private void ensureUniquePhone(Phone phone) {
        boolean phoneInUse = contractorRepository.existsByPhone(phone) || clientRepository.existsByPhone(phone);
        if (phoneInUse) {
            throw new DuplicateFieldException("Phone already in use");
        }
    }
}


