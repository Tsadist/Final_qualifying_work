package com.example.kyrsovay.controller;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.domain.Order;
import com.example.kyrsovay.domain.Client;
import com.example.kyrsovay.domain.enums.OrderStatus;
import com.example.kyrsovay.domain.enums.ClientRole;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.repository.ClientRepo;
import com.example.kyrsovay.service.OrderService;
import com.example.kyrsovay.service.TimeToString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Date;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final ClientRepo clientRepo;
    private final OrderRepo orderRepo;
    private final OrderService orderService;
    private final TimeToString timeToString;

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistration(Client client) {
        if (clientRepo.findByEmail(client.getEmail()) == null) {
//            client.setClientRole(ClientRole.Заказчик);
            clientRepo.save(client);
        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String getProfile(Model model,
                             @AuthenticationPrincipal ClientUserDetails userDetails) {
        Client client = userDetails.getClient();

        if (client.getEmail() != null) {
            if (client.getClientRole() == ClientRole.Заказчик) {
                List<Order> orderList = orderRepo.findAllByCustomerId(userDetails.getClient().getId());
                model.addAttribute("orders", orderList);
                model.addAttribute("time", timeToString);
                return "customerProfile";
            } else if (client.getClientRole() == ClientRole.Клинер ||
                    client.getClientRole() == ClientRole.Менеджер) {
                return "employeeProfile";
            }
        }
        log.info("Пользователь с таким Email не был найден");
        return "redirect:/login";
    }

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
