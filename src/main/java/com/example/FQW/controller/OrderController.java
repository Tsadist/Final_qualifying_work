package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.request.OrderRequest;
import com.example.FQW.models.response.MessageResponse;
import com.example.FQW.models.response.OrderResponse;
import com.example.FQW.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> getOrderFormId(@PathVariable Long orderId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrder(userDetails, orderId));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(orderService.getAllOrders(userDetails));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/create/order")
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(userDetails, orderRequest));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/edit/order/{orderId}")
    public ResponseEntity<OrderResponse> editOrder(@PathVariable Long orderId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.editOrder(userDetails, orderId, orderRequest));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/delete/order/{orderId}")
    public ResponseEntity<MessageResponse> deleteOrder(@PathVariable Long orderId,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(orderService.deleteOrder(userDetails, orderId));
    }

}