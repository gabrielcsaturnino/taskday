package com.example.taskday.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskday.auxiliary.Email;
import com.example.taskday.user.Client;
import com.example.taskday.user.CustomUserDetails;
import com.example.taskday.user.dto.CreateClientRequestDTO;
import com.example.taskday.user.service.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<String> createClient(@Valid @RequestBody CreateClientRequestDTO createClientDTO) {
        clientService.createClient(createClientDTO);
        return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<Client> getClientProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Client client = clientService.findByEmail(new Email(userDetails.getUsername()));
        return ResponseEntity.ok(client);
    }
}