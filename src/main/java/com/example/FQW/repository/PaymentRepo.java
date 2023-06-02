package com.example.FQW.repository;

import com.example.FQW.models.DB.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

    Payment findByOrderId (Long orderId);

}
