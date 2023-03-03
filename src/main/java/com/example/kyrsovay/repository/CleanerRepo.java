package com.example.kyrsovay.repository;

import com.example.kyrsovay.domain.Cleaner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CleanerRepo extends JpaRepository<Cleaner, Long> {

    Cleaner findByName(String name);
}