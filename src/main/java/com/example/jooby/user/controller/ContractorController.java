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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jooby.auxiliary.Email;
import com.example.jooby.user.Contractor;
import com.example.jooby.user.CustomUserDetails;
import com.example.jooby.user.dto.CreateContractorRequestDTO;
import com.example.jooby.user.dto.UpdateContractorRequestDTO;
import com.example.jooby.user.dto.ChangePasswordRequestDTO;
import com.example.jooby.user.service.ContractorService;

import jakarta.validation.Valid;
import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<Contractor> getContractorById(@PathVariable Long id) {
        Contractor contractor = contractorService.findById(id);
        return ResponseEntity.ok(contractor);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Contractor>> searchContractors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {
        List<Contractor> contractors = contractorService.searchContractors(name, location, minRating, maxRating);
        return ResponseEntity.ok(contractors);
    }

    @PutMapping("/profile")
    public ResponseEntity<Contractor> updateContractorProfile(
            @Valid @RequestBody UpdateContractorRequestDTO updateContractorDTO,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Contractor contractor = contractorService.findContractorByEmail(new Email(userDetails.getUsername()));
        
        Contractor updatedContractor = contractorService.updateContractor(contractor.getId(), updateContractorDTO);
        return ResponseEntity.ok(updatedContractor);
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordDTO,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Contractor contractor = contractorService.findContractorByEmail(new Email(userDetails.getUsername()));
        
        contractorService.changePassword(contractor.getId(), changePasswordDTO);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateAccount(@PathVariable Long id) {
        contractorService.deactivateAccount(id);
        return ResponseEntity.ok("Account deactivated successfully");
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateAccount(@PathVariable Long id) {
        contractorService.activateAccount(id);
        return ResponseEntity.ok("Account activated successfully");
    }
}