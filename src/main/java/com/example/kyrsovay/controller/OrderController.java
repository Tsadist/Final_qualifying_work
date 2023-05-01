package com.example.kyrsovay.controller;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.models.request.OrderRequest;
import com.example.kyrsovay.models.response.MessageResponse;
import com.example.kyrsovay.models.response.OrderResponse;
import com.example.kyrsovay.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> getOrderFormId(@PathVariable Long orderId,
                                                        @AuthenticationPrincipal ClientUserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrder(userDetails, orderId));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal ClientUserDetails userDetails) {
        return ResponseEntity.ok(orderService.getAllOrders(userDetails));
    }

    @PostMapping("/create/order")
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal ClientUserDetails userDetails,
                                                     @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(userDetails, orderRequest));
    }

    @PutMapping("/edit/order/{orderId}")
    public ResponseEntity<OrderResponse> editOrder(@PathVariable Long orderId,
                                                   @AuthenticationPrincipal ClientUserDetails userDetails,
                                                   @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.editOrder(userDetails, orderId, orderRequest));
    }

    @DeleteMapping("/delete/order/{orderId}")
    public ResponseEntity<MessageResponse> deleteOrder(@PathVariable Long orderId,
                                                       @AuthenticationPrincipal ClientUserDetails userDetails) {
        return ResponseEntity.ok(orderService.deleteOrder(userDetails, orderId));
    }

}
