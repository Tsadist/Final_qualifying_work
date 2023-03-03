package com.example.kyrsovay.controller;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.domain.Order;
import com.example.kyrsovay.domain.enums.OrderStatus;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Date;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final OrderRepo orderRepo;
    private final OrderService orderService;

    @GetMapping("/orderPage")
    public String getOrderPage() {
        return "orderPage";
    }

    @PostMapping("/orderPage")
    public String postOrderPage(@AuthenticationPrincipal ClientUserDetails userDetails,
                                Order order) {
        order.setCustomer(userDetails.getClient());
        order = orderRepo.save(order);
        orderService.calculateOrderDuration(order.getId());
        orderService.employeeAppointment(order.getId());
        orderService.pricing(order.getId());
        return "redirect:/profileCustomer";
    }

    @GetMapping("/editDateAndTime")
    public String getEditDateAndTime(Long id, Model model) {
        Order order = orderRepo.findById(id).orElse(null);
        model.addAttribute("order", order);
        return "/editDateAndTime";
    }

    @PostMapping("/editDateAndTime")
    public String postEditDateAndTime(Date theDate, Short startTime, Long id) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;

        order.setStartTime(startTime);
        order.setTheDate(theDate);
        orderRepo.save(order);
        orderService.employeeAppointment(id);

        return "redirect:/profile";
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
            order.setOrderStatus(OrderStatus.Оплачен);
        } else {
            order.setOrderStatus(OrderStatus.Ждет_оплаты);
        }
        orderRepo.save(order);
        return "redirect:/profile";
    }
}
