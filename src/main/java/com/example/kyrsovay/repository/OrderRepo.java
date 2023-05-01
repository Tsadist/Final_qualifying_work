package com.example.kyrsovay.repository;

import com.example.kyrsovay.models.DB.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId(Long id);

}
