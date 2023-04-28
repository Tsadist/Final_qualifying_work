package com.example.kyrsovay.controller;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.ex.RequestException;
import com.example.kyrsovay.models.DB.Order;
import com.example.kyrsovay.models.request.OrderRequest;
import com.example.kyrsovay.models.response.MessageResponse;
import com.example.kyrsovay.models.response.OrderResponse;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepo orderRepo;
    private final OrderService orderService;
    private MessageResponse messageResponse = new MessageResponse();
    private static Order order;


    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> getOrderFormId(@PathVariable Long orderId) {

        orderService.checkingOrderId(orderId);

        order = orderRepo.findById(orderId).get();
        return ResponseEntity.ok(orderService.createOrderResponse(order));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal ClientUserDetails userDetails) {

        List<Order> orderList = orderRepo.findAllByCustomerId(userDetails.getClient().getId());
        List<OrderResponse> orderResponseList = new ArrayList<>();
        orderList.forEach(order -> orderResponseList.add(orderService.createOrderResponse(order)));
        return ResponseEntity.ok(orderResponseList);
    }

    @PostMapping("/create/order")
    public ResponseEntity<Long> createOrder(@AuthenticationPrincipal ClientUserDetails userDetails,
                                            @RequestBody OrderRequest orderRequest) {

        return ResponseEntity
                .ok(orderService
                        .createOrder(userDetails, orderRequest));
    }

    @PutMapping("/edit/order/{orderId}")
    public ResponseEntity<MessageResponse> editOrder(@PathVariable Long orderId,
                                             @RequestBody OrderRequest orderRequest) {

        orderService.checkingOrderId(orderId);

        messageResponse.setMessage(String.valueOf(orderService
                .editOrder(orderId, orderRequest)));

        return ResponseEntity
                .ok(messageResponse);

    }

}
