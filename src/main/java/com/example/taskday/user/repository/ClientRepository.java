package com.example.taskday.user.repository;

import com.example.taskday.auxiliary.Cpf;
import com.example.taskday.auxiliary.Email;
import com.example.taskday.auxiliary.Phone;
import com.example.taskday.user.Client;

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
