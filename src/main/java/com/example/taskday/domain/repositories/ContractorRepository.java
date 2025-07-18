package com.example.taskday.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskday.domain.model.Contractor;
import com.example.taskday.domain.model.auxiliary.Email;

@Repository
public interface ContractorRepository extends JpaRepository <Contractor, Long> {
   Optional<Contractor> findByEmail(Email email);
   Contractor save(Contractor contractor);
}
