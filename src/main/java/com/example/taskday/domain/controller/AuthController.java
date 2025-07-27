package com.example.taskday.domain.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.taskday.domain.model.dtos.CreateAddressRequestDTO;
import com.example.taskday.domain.model.dtos.CreateContractorRequestDTO;
import com.example.taskday.domain.model.dtos.LoginRequestDTO;
import com.example.taskday.domain.service.AuthenticationService;
import com.example.taskday.domain.service.ContractorService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final ContractorService contractorService;

    public AuthController(AuthenticationService authenticationService, AuthenticationManager authenticationManager, ContractorService contractorService) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.contractorService = contractorService;
    }


    @PostMapping("/authenticate")
    public String authenticate(@RequestBody LoginRequestDTO loginRequest) {        
        Authentication authenticationRequest = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        return authenticationService.authenticate(authenticationRequest);
    }

    @PostMapping("/createContractorAccount")
    public String createAccount(@RequestBody CreateContractorRequestDTO createContractorDTO) {
        contractorService.createContractor(createContractorDTO);
        return "";
    }
    
    
    
}
