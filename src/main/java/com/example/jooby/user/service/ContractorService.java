package com.example.jooby.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.example.jooby.address.builder.AddressBuilder;
import com.example.jooby.address.dto.CreateAddressRequestDTO;
import com.example.jooby.address.repository.AddressRepository;
import com.example.jooby.auxiliary.Address;
import com.example.jooby.auxiliary.Cpf;
import com.example.jooby.auxiliary.Email;
import com.example.jooby.auxiliary.Password;
import com.example.jooby.auxiliary.Phone;
import com.example.jooby.auxiliary.Rating;
import com.example.jooby.exception.DuplicateFieldException;
import com.example.jooby.exception.NotFoundException;
import com.example.jooby.exception.NullValueException;
import com.example.jooby.user.Contractor;
import com.example.jooby.user.builder.ContractorBuilder;
import com.example.jooby.user.dto.ContractorPublicProfileDTO;
import com.example.jooby.user.dto.CreateContractorRequestDTO;
import com.example.jooby.user.dto.UpdateContractorRequestDTO;
import com.example.jooby.user.dto.ChangePasswordRequestDTO;
import com.example.jooby.user.repository.ClientRepository;
import com.example.jooby.user.repository.ContractorRepository;

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
    
    @Cacheable(value = "contractors", key = "#id")
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

    public void setAverageRating(Long id, Rating rating) {
        if (id == null || rating == null) {
            throw new NullValueException("Id or Rating cannot be null");
        }

        Contractor contractor = findById(id);
        Rating newRating = rating.setValue(contractor.getAverageRating(), 0.2);
        contractor.setAverageRating(newRating);
        contractorRepository.save(contractor);
    }

    public List<Contractor> findAllById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new NullValueException("Ids cannot be null or empty");
        }
        return contractorRepository.findAllById(ids);
    }

    @CacheEvict(value = "contractors", key = "#id")
    public Contractor updateContractor(Long id, UpdateContractorRequestDTO updateContractorDTO) {
        Contractor contractor = findById(id);
        
        // Verificar se email já está em uso por outro usuário
        if (!contractor.getEmailObject().getEmail().equals(updateContractorDTO.email())) {
            ensureUniqueEmail(new Email(updateContractorDTO.email()));
        }
        
        // Verificar se telefone já está em uso por outro usuário
        if (!contractor.getPhone().getPhoneNumber().equals(updateContractorDTO.phone())) {
            ensureUniquePhone(new Phone(updateContractorDTO.phone()));
        }
        
        contractor.setFirstName(updateContractorDTO.firstName());
        contractor.setLastName(updateContractorDTO.lastName());
        contractor.setPhone(new Phone(updateContractorDTO.phone()));
        contractor.setEmail(new Email(updateContractorDTO.email()));
        contractor.setDescription(updateContractorDTO.description());
        
        return contractorRepository.save(contractor);
    }

    public void changePassword(Long id, ChangePasswordRequestDTO changePasswordDTO) {
        Contractor contractor = findById(id);
        
        // Verificar se a senha atual está correta
        if (!passwordEncoder.matches(changePasswordDTO.currentPassword(), contractor.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        contractor.setPassword(Password.create(changePasswordDTO.newPassword(), passwordEncoder));
        contractorRepository.save(contractor);
    }

    public void deactivateAccount(Long id) {
        Contractor contractor = findById(id);
        contractor.setStatusAccount(false);
        contractorRepository.save(contractor);
    }

    public void activateAccount(Long id) {
        Contractor contractor = findById(id);
        contractor.setStatusAccount(true);
        contractorRepository.save(contractor);
    }

    public List<Contractor> searchContractors(String name, String location, Double minRating, Double maxRating) {
        // Implementar busca com critérios
        return contractorRepository.findAll();
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

