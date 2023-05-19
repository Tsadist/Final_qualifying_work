package com.example.FQW.repository;

import com.example.FQW.models.DB.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findAllById(Long id);

    Optional<User> findFirstByEmail(String username);
}
