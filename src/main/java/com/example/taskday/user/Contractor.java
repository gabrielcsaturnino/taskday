package com.example.taskday.user;

import java.util.ArrayList;
import java.util.List;

import com.example.taskday.auxiliary.Address;
import com.example.taskday.auxiliary.Cpf;
import com.example.taskday.auxiliary.DateOfBirthday;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Password;
import com.example.taskday.auxiliary.Phone;
import com.example.taskday.auxiliary.Rating;
import com.example.taskday.exception.NullValueException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    @Embedded
    private Rating avarageRating = new Rating(5.0);


    public Contractor() {
        super();
    }
    public Contractor(String first_name, String last_name, String rgDoc, Password password, Phone phone, Email email, Cpf cpf, DateOfBirthday dateOfBirthday) {
        super(first_name, last_name, phone, email, cpf, password, rgDoc, dateOfBirthday);
    }

    public void setAddress(Address address){
        if (address == null) {
            throw new NullValueException("Address cannot be null");
        }
        addresses.add(address);
        address.setContractor(this);
    }


    public void setAvarageRating(Rating newRating) {
        if (newRating == null) {
            throw new NullValueException("Rating cannot be null");   
        }
        this.avarageRating = newRating.setValue(this.avarageRating, 0.2);
    }

    public double getAvarageRating() {
        return avarageRating.getValue();
    }

    public Long getId() {
        return id;
    }

    


   
}
