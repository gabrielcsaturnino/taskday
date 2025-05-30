package com.example.taskday.domain.model;

import java.sql.Date;

import org.springframework.aot.generate.GenerationContext;

import com.example.taskday.domain.model.auxiliary.Adress;
import com.example.taskday.domain.model.auxiliary.Cpf;
import com.example.taskday.domain.model.auxiliary.DateOfBirthday;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.auxiliary.Phone;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String first_name;
    private String last_name;


    @Embedded
    private Phone phone;

    @Embedded
    private Email email;

    @Embedded
    private Cpf cpf;

    private String password;
    private boolean status_account;
    private String rg_doc;

    @Embedded
    private Adress adress;

    @Embedded
    private DateOfBirthday dateOfBirthday;


    public Client(){}

    public Client(String first_name, String last_name, Phone phone, Email email, Cpf cpf, String password, boolean status_account, String rg_doc, Adress adress, DateOfBirthday dateOfBirthday) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.status_account = status_account;
        this.rg_doc = rg_doc;
        this.adress = adress;
        this.dateOfBirthday = dateOfBirthday;
    }


    






}
