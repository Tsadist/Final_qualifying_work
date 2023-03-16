package com.example.kyrsovay.controller;

import com.example.kyrsovay.ex.RequestException;
import com.example.kyrsovay.models.Order;
import com.example.kyrsovay.models.enums.CleaningType;
import com.example.kyrsovay.models.enums.OrderStatus;
import com.example.kyrsovay.models.enums.RoomType;
import com.example.kyrsovay.models.response.OrderResponse;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final OrderRepo orderRepo;
    private final OrderService orderService;
    private static OrderResponse orderResponse;


    @PutMapping("/edit/order/{orderId}")
    public ResponseEntity<OrderResponse> postEditDateAndTime(@PathVariable Long orderId,
                                              @RequestParam Short startTime,
                                              @RequestParam(required = false) Date theDate,
                                              @RequestParam(required = false) Float area,
                                              @RequestParam(required = false) CleaningType cleaningType,
                                              @RequestParam(required = false) RoomType roomType) {

        if (orderRepo.findById(orderId).isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Заказ с таким ID не найден");
        }

        if (theDate != null && theDate.toInstant().isBefore(Instant.from(LocalDate.now())) ||
                startTime != null && (startTime < 8 || startTime > 22) ||
                area != null && area <= 0 ||
                cleaningType != null && !Arrays.toString(CleaningType.values()).contains(cleaningType.toString()) ||
                roomType != null && !Arrays.toString(RoomType.values()).contains(roomType.toString()))
            throw new RequestException(HttpStatus.BAD_REQUEST, "Введены невалидные данный при ропытке изменения заказа");


        orderResponse = orderService.putField(orderId, startTime, theDate, area, cleaningType, roomType);
        orderService.employeeAppointment(orderId);

        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/paymentForm")
    public String getPaymentForm(Long id, Model model) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;

        model.addAttribute("cost", order.getCost());
        return "/paymentForm";
    }

    @PostMapping("/paymentForm")
    public String postPaymentForm(Long cardNumber, Long id) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;

        if (cardNumber >= 1111111111111111L) {
            order.setOrderStatus(OrderStatus.PAID);
        } else {
            order.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        }
        orderRepo.save(order);
        return "redirect:/profile";
    }
}
