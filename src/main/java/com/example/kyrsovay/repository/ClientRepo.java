package com.example.kyrsovay.repository;

import com.example.kyrsovay.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepo extends JpaRepository <Client, Long> {

    Client findByEmail(String email);

    List<Client> findAllById(Long id);

}
