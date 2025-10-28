package com.example.jooby.user.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.jooby.auxiliary.Email;
import com.example.jooby.user.Client;
import com.example.jooby.user.Contractor;
import com.example.jooby.user.CustomUserDetails;
import com.example.jooby.user.User;
import com.example.jooby.user.repository.ClientRepository;
import com.example.jooby.user.repository.ContractorRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


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
