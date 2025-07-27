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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "client")
@Entity
public class Client extends User{
    
    @Id
    @Column(name = "id_client")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();
    

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    
    public Client(){
        super();
    }

    public Client(String first_name, String last_name, Phone phone, Email email, Cpf cpf, Password password, String rgDoc, DateOfBirthday dateOfBirthday) {
        super(first_name, last_name, phone, email, cpf, password,  rgDoc, dateOfBirthday);
    }



    public void setAddress(Address address){
         if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        addresses.add(address);
        address.setClient(this);
    }


    public void setJob(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job cannot be null");   
        }
        this.jobs.add(job);
        
    }

    public Long getId() {
        return id;
    }



}
