package com.example.taskday.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.taskday.auxiliary.Cpf;
import com.example.taskday.auxiliary.DateOfBirthday;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Password;
import com.example.taskday.auxiliary.Phone;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class User {

    @Column(name = "first_name", nullable = false)
    private String first_name;
    @Column(name = "last_name", nullable = false)
    private String last_name;

    
    @Embedded
    private Phone phone;
    
    @Embedded
    private Email email;
    
    @Embedded
    private Cpf cpf;
    
    @Embedded
    private Password password;

    @Column(name = "status_account", nullable = false)
    private boolean status_account;

    @Column(name = "rg_doc", nullable = false, unique = true)
    private String rgDoc;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date_of_birth", nullable = false))
    private DateOfBirthday dateOfBirthday;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;


    public User() {}
    public User(String first_name, String last_name, Phone phone, Email email, Cpf cpf, Password password, String rgDoc, DateOfBirthday dateOfBirthday) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.status_account = true;
        this.rgDoc = rgDoc;
        this.dateOfBirthday = dateOfBirthday;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public Phone getPhone() {
        return phone;
    }

    public Email getEmailObject() {
        return email;
    }
    public String getEmail() {
        return email.getEmail();
    }
    public String getCpf() {
        return cpf.getValue();
    }
    public String getPassword() {
        return password.getPassword();
    }
    public boolean isStatus_account() {
        return status_account;
    }
    public String getRgDoc() {
        return rgDoc;
    }
    public DateOfBirthday getDateOfBirthday() {
        return dateOfBirthday;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    
    
} 
