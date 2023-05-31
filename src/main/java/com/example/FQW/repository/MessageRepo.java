package com.example.FQW.repository;

import com.example.FQW.models.DB.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

    void deleteAllByChatId (Long chatId);

    List<Message> findAllByChatId(Long chatId);
}
