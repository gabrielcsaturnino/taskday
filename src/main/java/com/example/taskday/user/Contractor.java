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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contractor extends User {
    

    @Id
    @Column(name = "id_contractor")  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @Embedded
    private Rating averageRating = new Rating(5.0);

    @Column(name = "description", nullable = false)
    private String description;

    public Contractor() {
        super();
    }
    public Contractor(String first_name, String last_name, String rgDoc, Password password, Phone phone, Email email, Cpf cpf, DateOfBirthday dateOfBirthday, String description) {
        super(first_name, last_name, phone, email, cpf, password, rgDoc, dateOfBirthday);
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new NullValueException("Description cannot be null or blank");
        }
        this.description = description;
    }
    public void setAddress(Address address){
        if (address == null) {
            throw new NullValueException("Address cannot be null");
        }
        addresses.add(address);
        address.setContractor(this);
    }


    public void setAverageRating(Rating newRating) {
        if (newRating == null) {
            throw new NullValueException("Rating cannot be null");   
        }
        this.averageRating = newRating;
    }

    public Rating getAverageRating() {
        return averageRating;
    }

    public Long getId() {
        return id;
    }

    


   
}
