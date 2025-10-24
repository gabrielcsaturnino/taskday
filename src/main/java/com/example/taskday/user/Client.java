package com.example.taskday.user;
import java.util.ArrayList;
import java.util.List;

import com.example.taskday.auxiliary.Address;
import com.example.taskday.auxiliary.Cpf;
import com.example.taskday.auxiliary.DateOfBirthday;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Password;
import com.example.taskday.auxiliary.Phone;
import com.example.taskday.exception.NullValueException;
import com.example.taskday.job.Job;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
            throw new NullValueException("Address cannot be null");
        }
        addresses.add(address);
        address.setClient(this);
    }


    public void setJob(Job job) {
        if (job == null) {
            throw new NullValueException("Job cannot be null");   
        }
        this.jobs.add(job);
        
    }

    public Long getId() {
        return id;
    }


    // remover esse metodo
    public void setId(Long id) {
        this.id = id;
    }
    


}
