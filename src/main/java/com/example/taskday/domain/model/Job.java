package com.example.taskday.domain.model;

import com.example.taskday.domain.model.auxiliary.Address;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private int pricePerHour;
     
    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;
}
