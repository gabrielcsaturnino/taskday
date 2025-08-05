package com.example.taskday.user.builder;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.taskday.auxiliary.Cpf;
import com.example.taskday.auxiliary.DateOfBirthday;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Password;
import com.example.taskday.auxiliary.Phone;
import com.example.taskday.user.Client;
import com.example.taskday.user.dto.CreateClientRequestDTO;

public class ClientBuilder {
    private String firstName;
    private String lastName;
    private String rgDocument;
    private Password password;
    private Phone phone;
    private Email email;
    private Cpf cpf;
    private DateOfBirthday dateOfBirthday;

    private final PasswordEncoder passwordEncoder;

    public ClientBuilder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public ClientBuilder fromDTO(CreateClientRequestDTO createClientDTO) {
        this.firstName = createClientDTO.firstName();
        this.lastName = createClientDTO.lastName();
        this.rgDocument = createClientDTO.rgDocument();
        this.password = Password.create(createClientDTO.password(), passwordEncoder);
        this.phone = new Phone(createClientDTO.phone());
        this.email = new Email(createClientDTO.email());
        this.cpf = new Cpf(createClientDTO.cpf());
        this.dateOfBirthday = new DateOfBirthday(createClientDTO.dateOfBirthday());
        return this;
    }

    public Client build() {
        return new Client(firstName, lastName, phone, email, cpf, password, rgDocument, dateOfBirthday);
    }
}
