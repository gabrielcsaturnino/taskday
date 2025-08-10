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
import com.example.taskday.user.Contractor;
import com.example.taskday.user.CustomUserDetails;
import com.example.taskday.user.dto.CreateContractorRequestDTO;
import com.example.taskday.user.service.ContractorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contractors")
public class ContractorController {

    private final ContractorService contractorService;

    public ContractorController(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    @PostMapping
    public ResponseEntity<String> createContractor(@Valid @RequestBody CreateContractorRequestDTO createContractorDTO) {
        contractorService.createContractor(createContractorDTO);
        return new ResponseEntity<>("Contractor created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<Contractor> getContractorProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Contractor contractor = contractorService.findContractorByEmail(new Email(userDetails.getUsername()));
        return ResponseEntity.ok(contractor);
    }
}