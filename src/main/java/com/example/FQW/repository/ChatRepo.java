package com.example.FQW.repository;

import com.example.FQW.models.DB.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Long> {

    List<Chat> findAllByCreateUserId (Long customerId);
}
