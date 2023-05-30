package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.request.OrderRequest;
import com.example.FQW.models.response.AdditionServiceResponse;
import com.example.FQW.models.response.MessageResponse;
import com.example.FQW.models.response.OrderResponse;
import com.example.FQW.service.AdditionServiceService;
import com.example.FQW.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AdditionServiceService additionServiceService;

    @GetMapping("/addition_services")
    public ResponseEntity<List<AdditionServiceResponse>> getAdditionServices(){
        return ResponseEntity.ok(additionServiceService.getAdditionService());
    }

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
    @PostMapping("/order/create")
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(userDetails, orderRequest));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/order/{orderId}/edit")
    public ResponseEntity<OrderResponse> editOrder(@PathVariable Long orderId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.editOrder(userDetails, orderId, orderRequest));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/order/{orderId}/delete")
    public ResponseEntity<MessageResponse> deleteOrder(@PathVariable Long orderId,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(orderService.deleteOrder(userDetails, orderId));
    }

}
