package com.example.jooby.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.jooby.infra.security.service.AuthenticationService;
import com.example.jooby.user.dto.LoginRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    
    public AuthController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequestDTO loginRequest) {        
        Authentication authenticationRequest = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        String token = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(token);
    }

    
    
}
