package com.example.taskday.domain.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.Contractor;
import com.example.taskday.domain.model.CustomUserDetails;
import com.example.taskday.domain.model.User;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.repositories.ClientRepository;
import com.example.taskday.domain.repositories.ContractorRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{


    private final ClientRepository clientRepository;
    private final ContractorRepository contractorRepository;

    public UserDetailsServiceImpl(ClientRepository clientRepository, ContractorRepository contractorRepository) {
        this.clientRepository = clientRepository;
        this.contractorRepository = contractorRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
 
        Email emailToSearch;
        emailToSearch = new Email(username);
        
        Optional<? extends User> userOptional = clientRepository
        .findByEmail(emailToSearch).map(user -> (User) user)
        .or(() -> contractorRepository.findByEmail(emailToSearch).map(user -> (User) user));
        
        return userOptional
            .map(user -> {
                if (user instanceof Contractor) {
                    return new CustomUserDetails(null, (Contractor) user);
                } else {
                    return new CustomUserDetails((Client) user, null);
                }
            })
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

    }

   
}
