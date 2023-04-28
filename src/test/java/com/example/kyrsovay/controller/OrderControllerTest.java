package com.example.kyrsovay.controller;

import com.example.kyrsovay.models.DB.Order;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.repository.ScheduledRepo;
import com.example.kyrsovay.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
public class OrderControllerTest {

    private final OrderRepo orderRepo;
    private final ScheduledRepo scheduledRepo;

//    @Test
//    public void isOrderCreate() {
//        OrderService orderService = new OrderService(orderRepo, scheduledRepo);
//        Long orderId = orderService.createOrder();
//        Order order = orderRepo.findById(orderId);
//    }

}
