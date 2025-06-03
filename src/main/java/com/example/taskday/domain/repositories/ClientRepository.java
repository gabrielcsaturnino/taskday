package com.example.taskday.domain.repositories;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.auxiliary.Email;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client save(Client client);
    Optional findById(Long id);
    Optional<Client> findByEmail(Email email);
}
