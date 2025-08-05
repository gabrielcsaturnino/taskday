package com.example.taskday.user.builder;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.taskday.auxiliary.Cpf;
import com.example.taskday.auxiliary.DateOfBirthday;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Password;
import com.example.taskday.auxiliary.Phone;
import com.example.taskday.user.Contractor;
import com.example.taskday.user.dto.CreateContractorRequestDTO;


public class ContractorBuilder {
    private String firstName;
    private String lastName;
    private String rgDocument;
    private Password password;
    private Phone phone;
    private Email email;
    private Cpf cpf;
    private DateOfBirthday dateOfBirthday;  
    
    private final PasswordEncoder passwordEncoder;
    
    
    public ContractorBuilder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public ContractorBuilder fromDTO(CreateContractorRequestDTO createContractorDTO) {
        this.firstName = createContractorDTO.firstName();
        this.lastName = createContractorDTO.lastName();
        this.rgDocument = createContractorDTO.rgDocument();
        this.password = Password.create(createContractorDTO.password(), passwordEncoder);
        this.phone = new Phone(createContractorDTO.phone());
        this.email = new Email(createContractorDTO.email());
        this.cpf = new Cpf(createContractorDTO.cpf());
        this.dateOfBirthday = new DateOfBirthday(createContractorDTO.dateOfBirthday());
        return this;
    }

    public Contractor build() {
        return new Contractor(firstName, lastName, rgDocument, password, phone, email, cpf, dateOfBirthday);
    }
        
}
