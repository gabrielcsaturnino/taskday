package com.example.taskday.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.taskday.domain.model.auxiliary.Address;
import com.example.taskday.domain.model.auxiliary.Cpf;
import com.example.taskday.domain.model.auxiliary.DateOfBirthday;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.auxiliary.Phone;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "contractor") 
@Entity
public class Contractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contractor") 
    private Long id;

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
    

    @Column(name = "hash_password", nullable = false)
    private String password;
    
    @Column(name = "status_account", nullable = false)
    private boolean status_account;
    
    @Column(name = "rg_contractor", nullable = false, unique = true)
    private String rg_doc;

    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;


    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date_of_birth", nullable = false))
    private DateOfBirthday dateOfBirthday;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    

    public Contractor() {}
    public Contractor(String first_name, String last_name, Phone phone, Email email, Cpf cpf, String password, boolean status_account, String rg_doc, DateOfBirthday dateOfBirthday) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.status_account = status_account;
        this.rg_doc = rg_doc;
        this.dateOfBirthday = dateOfBirthday;
    }

    public void addAddress(Address address){
        if(address != null) {
            address.setContractor(this);
            this.addresses.add(address);
        }
    }
}
