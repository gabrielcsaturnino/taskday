package com.example.taskday.domain.controller;

import com.example.taskday.domain.exception.PasswordException;
import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.Contractor;
import com.example.taskday.domain.model.User;
import com.example.taskday.domain.model.auxiliary.*;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.ContractorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/main")
public class MainController {

    @Autowired
    ContractorRepository contractorRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Contractor newContractor; 

     @PostConstruct
     public void init(){
    
         String firstName = "Fulano";
         String lastName = "De Tal";
         Password passwordHash = new Password("senhaSegura123"); // This should be encoded using passwordEncoder
         boolean isActiveAccount = true;
         String rgDocument = "3456789";

         Phone randomPhone = new Phone("62912345678");
         Email randomEmail = new Email("fulano.tal@example.com");
         Cpf randomCpf = new Cpf("08063359127");
         DateOfBirthday randomDob = new DateOfBirthday(LocalDate.of(1990, 1, 1));

         Contractor newContractor =  new Contractor(
                 firstName,
                 lastName,
                 rgDocument,
                 passwordHash,
                 isActiveAccount,
                 randomPhone,
                 randomEmail,
                 randomCpf,
                 randomDob
         );

         Address address1 = new Address("Rua Exemplo", "123", "Apto 456", "Bairro Exemplo", "Cidade Exemplo", "12345-678", newContractor);

         newContractor.setAddress(address1);
         try {
         contractorRepository.save(newContractor);
         } catch (IllegalArgumentException e) {
             System.out.println("Erro ao definir o endereço: " + e.getMessage());
         }
     }

    @GetMapping("/get")
    public Contractor getMethodName() throws Throwable {
        return (Contractor) contractorRepository.findByEmail(new Email("fulano.tal@example.com"))
                 .orElseThrow(() -> new RuntimeException("Client not found"));
    }
}