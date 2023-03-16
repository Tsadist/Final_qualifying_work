package com.example.kyrsovay.controller;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.ex.RequestException;
import com.example.kyrsovay.models.Order;
import com.example.kyrsovay.models.request.OrderRequest;
import com.example.kyrsovay.models.response.IdResponse;
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
    private IdResponse idResponse = new IdResponse();
    private static Order order;


    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> getOrderFormId(@PathVariable Long orderId) {
        if(orderId <= 0) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "ID меньше нуля");
        }
        if (orderRepo.findById(orderId).isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Заказ с таким ID не найден");
        }

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
    public ResponseEntity<IdResponse> createOrder(@AuthenticationPrincipal ClientUserDetails userDetails,
                                                  @RequestBody OrderRequest orderRequest) {

        if (orderRequest.getArea() == null || orderRequest.getStartTime() == null ||
                orderRequest.getCleaningType() == null || orderRequest.getRoomType() == null ||
                orderRequest.getTheDate() == null)
            throw new RequestException(HttpStatus.BAD_REQUEST, "Какое-то из полей не было передано");

        order = new Order();
        order.setCustomer(userDetails.getClient());
        order.setArea(orderRequest.getArea());
        order.setRoomType(orderRequest.getRoomType());
        order.setCleaningType(orderRequest.getCleaningType());
        order.setTheDate(orderRequest.getTheDate());
        order.setStartTime(orderRequest.getStartTime());
        order = orderRepo.save(order);

        orderService.calculateOrderDuration(order.getId());
        orderService.employeeAppointment(order.getId());
        orderService.pricing(order.getId());

        idResponse.setId(order.getId());

        return ResponseEntity.ok(idResponse);
    }
}
