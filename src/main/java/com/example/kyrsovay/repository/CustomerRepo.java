package com.example.kyrsovay.repository;

import com.example.kyrsovay.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository <Customer, Long> {

    Customer findByEmail(String email);

    Customer findByUsername(String username);

}
