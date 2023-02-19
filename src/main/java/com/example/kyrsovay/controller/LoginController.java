package com.example.kyrsovay.controller;

import com.example.kyrsovay.domain.Customer;
import com.example.kyrsovay.domain.Employee;
import com.example.kyrsovay.repository.CustomerRepo;
import com.example.kyrsovay.repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final CustomerRepo customerRepo;
    private final EmployeeRepo employeeRepo;

    @GetMapping("/login")
    public String getRegistration() {
        return "login";
    }

    @PostMapping("/login")
    public String postRegistration(Employee employee, Customer customer) {
        if (employeeRepo.findByEmail(employee.getEmail()) != null) {
            return "redirect:/employeeProfile";
        } else if (customerRepo.findByEmail(customer.getEmail()) != null){
            return "redirect:/customerProfile";
        }
        return "redirect:/registration";

    }
}
