package com.example.jooby.auxiliary;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.jooby.job.Job;
import com.example.jooby.user.Client;
import com.example.jooby.user.Contractor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


enum AddressOwnerType {
    CLIENT,
    CONTRACTOR,
    JOB
}

@Entity
@Table(name = "address")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Integer idAddress;

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "number", nullable = false, length = 10)
    private String number;

    @Column(name = "neighborhood", nullable = false, length = 50)
    private String neighborhood;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Enumerated(EnumType.STRING) 
    @Column(name = "owner_type", nullable = false, length = 30)
    private AddressOwnerType ownerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", referencedColumnName = "id_client") 
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contractor", referencedColumnName = "id_contractor") 
    private Contractor contractor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_job", referencedColumnName = "id_job") 
    private Job job;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
   

    public Address(String street, String number, String neighborhood, String city, String state, String zipCode, Client client) {
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.client = client;
        this.ownerType = AddressOwnerType.CLIENT;
    }

    public Address(String street, String number, String neighborhood, String city, String state, String zipCode, Contractor contractor) {
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.contractor = contractor;
        this.ownerType = AddressOwnerType.CONTRACTOR;
    }

    public Address(String street, String number, String neighborhood, String city, String state, String zipCode, Job job) {
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.job = job; // Associa o job
        this.ownerType = AddressOwnerType.JOB; // Define o tipo de dono
    }

    public Address() {

    }

    public void setClient(Client client) {
        this.client = client;
        this.ownerType = AddressOwnerType.CLIENT;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
        this.ownerType = AddressOwnerType.CONTRACTOR;
    }
    public void setJob(Job job) {
        this.job = job;
        this.ownerType = AddressOwnerType.JOB;
    }

    public AddressOwnerType getOwnerType() {
        return ownerType;
    }

    public Job getJob() {
        return job;
    }

    public Client getClient() {
        return client;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

}