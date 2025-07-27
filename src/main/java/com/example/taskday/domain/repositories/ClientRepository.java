package com.example.taskday.domain.repositories;

import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.auxiliary.Cpf;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.auxiliary.Phone;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client save(Client client);
    Optional findById(Long id);
    boolean existsByEmail(Email email);
    boolean existsByrgDoc(String rgDoc);
    boolean existsByCpf(Cpf cpf);
    boolean existsByPhone(Phone phone);
    Optional<Client> findByEmail(Email email);
}
