package com.example.jooby.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jooby.auxiliary.Cpf;
import com.example.jooby.auxiliary.Email;
import com.example.jooby.auxiliary.Phone;
import com.example.jooby.user.Contractor;

@Repository
public interface ContractorRepository extends JpaRepository <Contractor, Long> {
   Optional<Contractor> findByEmail(Email email);
   Contractor save(Contractor contractor);
   boolean existsByEmail(Email email);
   boolean existsByrgDoc(String rgDoc);
   boolean existsByCpf(Cpf cpf);
   boolean existsByPhone(Phone phone);
   Optional<Contractor> findById(Long id);
}
