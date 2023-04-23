package com.example.kyrsovay.repository;

import com.example.kyrsovay.models.DB.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepo extends JpaRepository <Client, Long> {

    Client findByEmail(String email);

    List<Client> findAllById(Long id);

    Optional<Client> findFirstByEmail(String username);
}
