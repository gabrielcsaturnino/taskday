package com.example.taskday.address.builder;

import com.example.taskday.address.dto.CreateAddressRequestDTO;
import com.example.taskday.auxiliary.Address;
import com.example.taskday.exception.NotFoundException;
import com.example.taskday.job.Job;
import com.example.taskday.user.Client;
import com.example.taskday.user.Contractor;

public class AddressBuilder {
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private Object owner; 

    public AddressBuilder fromDTO(CreateAddressRequestDTO dto) {
        this.street = dto.street();
        this.number = dto.number();
        this.neighborhood = dto.neighborhood();
        this.city = dto.city();
        this.state = dto.state();
        this.zipCode = dto.zipCode();
        return this;
    }

    public AddressBuilder withOwner(Contractor contractor) {
        this.owner = contractor;
        return this;
    }

    public AddressBuilder withOwner(Client client) {
        this.owner = client;
        return this;
    }

    public AddressBuilder withOwner(Job job) {
        this.owner = job;
        return this;
    }

    public Address build() {
        if (owner instanceof Contractor contractor) {
            return new Address(street, number, neighborhood, city, state, zipCode, contractor);
        } else if (owner instanceof Client client) {
            return new Address(street, number, neighborhood, city, state, zipCode, client);
        } else if (owner instanceof Job job) {
            return new Address(street, number, neighborhood, city, state, zipCode, job);
        } else {
            throw new NotFoundException("Invalid owner type");
        }
    }
}
