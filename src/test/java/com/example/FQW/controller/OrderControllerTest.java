package com.example.FQW.controller;

import com.example.FQW.repository.OrderRepo;
import com.example.FQW.repository.ScheduledRepo;
import lombok.RequiredArgsConstructor;
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
