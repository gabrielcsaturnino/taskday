package com.example.jooby.user.repository;

import com.example.jooby.auxiliary.Cpf;
import com.example.jooby.auxiliary.Email;
import com.example.jooby.auxiliary.Phone;
import com.example.jooby.user.Client;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client save(Client client);
    Optional<Client> findById(Long id);
    boolean existsByEmail(Email email);
    boolean existsByrgDoc(String rgDoc);
    boolean existsByCpf(Cpf cpf);
    boolean existsByPhone(Phone phone);
    Optional<Client> findByEmail(Email email);
}
