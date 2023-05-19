package com.example.FQW.controller;

import com.example.FQW.models.DB.Order;
import com.example.FQW.models.enums.OrderStatus;
import com.example.FQW.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final OrderRepo orderRepo;

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
