package com.example.healthcaremanagement.repository;

import com.example.healthcaremanagement.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
