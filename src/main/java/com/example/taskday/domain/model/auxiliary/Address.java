package com.example.taskday.domain.model.auxiliary;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.Contractor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


enum AddressOwnerType {
    CLIENT,
    CONTRACTOR
}

@Entity
@Table(name = "address")
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

    @Enumerated(EnumType.STRING) // Ou apenas @Column(name = "owner_type") se usar String diretamente
    @Column(name = "owner_type", nullable = false, length = 30)
    private AddressOwnerType ownerType;

    // Relacionamento com Client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", referencedColumnName = "id_client") // FK na tabela address
    private Client client;

    // Relacionamento com Contractor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contractor", referencedColumnName = "id_contractor") // FK na tabela address
    private Contractor contractor;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor para endereço de Cliente
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

    // Construtor para endereço de Prestador
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
}