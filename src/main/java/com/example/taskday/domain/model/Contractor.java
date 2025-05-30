package com.example.taskday.domain.model;

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
public class Contractor {
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
}
