package com.example.FQW.repository;

import com.example.FQW.models.DB.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId(Long id);

}
