package com.example.FQW.repository;

import com.example.FQW.models.DB.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId (Long customerId);

    List<Order> findAllByCleanerId (Long cleanerId);

    List<Order> findAllByTheDateAndCleanerId(Date theDate, Long cleaner_id);
}
