package com.example.kyrsovay.controller;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.domain.Client;
import com.example.kyrsovay.domain.Order;
import com.example.kyrsovay.domain.enums.ClientRole;
import com.example.kyrsovay.repository.ClientRepo;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.service.ManagerService;
import com.example.kyrsovay.service.TimeToString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BaseController {

    private final ClientRepo clientRepo;
    private final OrderRepo orderRepo;
    private final TimeToString timeToString;
    private final ManagerService managerService;

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
            client.setClientRole(ClientRole.Заказчик);
            clientRepo.save(client);
        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String getProfile(Model model,
                             @AuthenticationPrincipal ClientUserDetails userDetails) {

        switch (userDetails.getClient().getClientRole()){
            case Заказчик:
                List<Order> orderList = orderRepo.findAllByCustomerId(userDetails.getClient().getId());
                model.addAttribute("orders", orderList);
                model.addAttribute("time", timeToString);
                return "profileCustomer";
            case Клинер:
                return "profileCleaner";
            case Менеджер:
                model.addAttribute("cleanerList", managerService.getSortedListCleaner(userDetails));
                return "profileManager";
            default:
                log.info("Пользователь с таким Email не был найден");
                return "redirect:/login";
        }
    }
}
