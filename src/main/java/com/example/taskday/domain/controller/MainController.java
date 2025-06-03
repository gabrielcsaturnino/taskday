package com.example.taskday.domain.controller;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.auxiliary.*;
import com.example.taskday.domain.repositories.ClientRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/main")
public class MainController {


    @Autowired
    ClientRepository clientRepository;

    private Client newClient; 

    @PostConstruct
    public void init() {
    
    String firstName = "Fulano";
    String lastName = "De Tal";
    String passwordHash = "senhaSuperSegura123Hash"; // Em um cenário real, seria um hash gerado
    boolean isActiveAccount = true;
    String rgDocument = "3456789";

    Phone randomPhone = new Phone("62912345678");
    Email randomEmail = new Email("fulano.tal@example.com");
    Cpf randomCpf = new Cpf("08063359127");
    DateOfBirthday randomDob = new DateOfBirthday(LocalDate.of(1990, 1, 1)); // Data de nascimento aleatória
    List<Address> clientAddresses = new ArrayList<>();

    

    Client newClient = new Client(
            firstName,
            lastName,
            randomPhone,
            randomEmail,
            randomCpf,
            passwordHash,
            isActiveAccount,
            rgDocument,
            randomDob);

      
    newClient.addAddress(new Address(
                "Rua das Flores", "456", "Centro", "Cidade Exemplo", "EX", "12345-678",
                newClient
        ));

        clientRepository.save(newClient);


}

    @GetMapping("/")
     public Client getMethodName() throws Throwable {
         return (Client) clientRepository.findByEmail(new Email("fulano.tal@example.com"))
                 .orElseThrow(() -> new RuntimeException("Client not found"));
     }
     
}
