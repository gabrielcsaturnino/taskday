package com.example.taskday.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.repositories.ClientRepository;

@Service
class ClienteService {
    
    @Autowired
    private ClientRepository clientRepository;

    public void createClient(Client client){
        if(client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }

        clientRepository.save(client);
    }


}