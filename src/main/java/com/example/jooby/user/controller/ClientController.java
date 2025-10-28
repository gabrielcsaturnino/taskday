package com.example.jooby.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jooby.auxiliary.Email;
import com.example.jooby.user.Client;
import com.example.jooby.user.CustomUserDetails;
import com.example.jooby.user.dto.CreateClientRequestDTO;
import com.example.jooby.user.dto.UpdateClientRequestDTO;
import com.example.jooby.user.dto.ChangePasswordRequestDTO;
import com.example.jooby.user.service.ClientService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/profile")
    public ResponseEntity<Client> updateClientProfile(
            @Valid @RequestBody UpdateClientRequestDTO updateClientDTO,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Client client = clientService.findByEmail(new Email(userDetails.getUsername()));
        
        Client updatedClient = clientService.updateClient(client.getId(), updateClientDTO);
        return ResponseEntity.ok(updatedClient);
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordDTO,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Client client = clientService.findByEmail(new Email(userDetails.getUsername()));
        
        clientService.changePassword(client.getId(), changePasswordDTO);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateAccount(@PathVariable Long id) {
        clientService.deactivateAccount(id);
        return ResponseEntity.ok("Account deactivated successfully");
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateAccount(@PathVariable Long id) {
        clientService.activateAccount(id);
        return ResponseEntity.ok("Account activated successfully");
    }
}