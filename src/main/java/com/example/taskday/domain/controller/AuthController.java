package com.example.taskday.domain.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.taskday.domain.model.LoginRequestDTO;
import com.example.taskday.domain.service.AuthenticationService;

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

    public AuthController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/authenticate")
    public String postMethodName(@RequestBody LoginRequestDTO loginRequest) {        
        Authentication authenticationRequest = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        System.out.println("Autenticando usuário: " + authenticationRequest.getName());
        System.out.println(authenticationRequest.getAuthorities().stream().map(Object::toString).toList());
        return authenticationService.authenticate(authenticationRequest);
    }
    

    @GetMapping("/methodName")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
}
