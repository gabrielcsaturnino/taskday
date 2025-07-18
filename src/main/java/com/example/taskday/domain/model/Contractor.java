package com.example.taskday.domain.model;

import java.util.ArrayList;
import java.util.List;


import com.example.taskday.domain.model.auxiliary.Address;
import com.example.taskday.domain.model.auxiliary.Cpf;
import com.example.taskday.domain.model.auxiliary.DateOfBirthday;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.auxiliary.Password;
import com.example.taskday.domain.model.auxiliary.Phone;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "contractor") 
@Entity
public class Contractor extends User {
    

    @Id
    @Column(name = "id_contractor")  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "avarage_rating", nullable = false, columnDefinition = "DECIMAL(2,1)")
    private double avarageRating = 5.0;


    public Contractor() {
        super();
    }
    public Contractor(String first_name, String last_name, String rg_doc, Password password, boolean status_account, Phone phone, Email email, Cpf cpf, DateOfBirthday dateOfBirthday) {
        super(first_name, last_name, phone, email, cpf, password, status_account, rg_doc, dateOfBirthday);
    }

    public void setAddress(Address address){
         if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        addresses.add(address);
        address.setContractor(this);
    }


    public void setAvarageRating(double newAvarageRating) {
        if (newAvarageRating < 0.0 || newAvarageRating > 5.0) {
            throw new IllegalArgumentException("Average rating must be between 0.0 and 5.0");
        }
        this.avarageRating = newAvarageRating;
    }

    public double getAvarageRating() {
        return avarageRating;
    }

    public Long getId() {
        return id;
    }

    


   
}
