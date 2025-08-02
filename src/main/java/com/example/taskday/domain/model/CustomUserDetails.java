package com.example.taskday.domain.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {


    private final Client client;
    private final Contractor contractor;

    public CustomUserDetails(Client client, Contractor contractor) {
        this.client = client;
        this.contractor = contractor;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role;
        if(this.client != null) {
            role = "CLIENT";
        }
        if(this.contractor != null) {
            role = "CONTRACTOR";
        } 
        
        else {
            role = "USER";
        }

        return Collections.singletonList(new SimpleGrantedAuthority(role));
        
    }

    @Override
    public String getPassword() {
        if(client != null) {
            return client.getPassword();
        }
        if(contractor != null) {
            return contractor.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if(client != null) {
            return client.getEmail();
        }
        if(contractor != null) {
            return contractor.getEmail();
        }
        return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        if(client != null) {
            return client.isStatus_account();
        }
        if(contractor != null) {
            return contractor.isStatus_account();
        }
        return false; 
    }

    @Override
    public boolean isAccountNonLocked() {
        if(client != null) {
            return client.isStatus_account();
        }
        if(contractor != null) {
            return contractor.isStatus_account();
        }
        return false; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if(client != null) {
            return client.isStatus_account();
        }
        if(contractor != null) {
            return contractor.isStatus_account();
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        if(client != null) {
            return client.isStatus_account();
        }
        if(contractor != null) {
            return contractor.isStatus_account();
        }
        return false; 
    }

    public Long getUserId() {
        if(client != null) {
            return client.getId();
        }
        if(contractor != null) {
            return contractor.getId();
        }
        return null; 
    }
}