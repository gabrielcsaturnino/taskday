package com.example.jooby.address.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jooby.auxiliary.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address save(Address address);
    Optional<Address> findById(Long id);
    void deleteById(Long id);

}
