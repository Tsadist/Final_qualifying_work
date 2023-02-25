package com.example.kyrsovay.controller;

import com.example.kyrsovay.repository.ClientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final ClientRepo clientRepo;

//    @GetMapping("/login")
//    public String getRegistration() {
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String postRegistration(Employee employee, Customer users) {
//        if (employeeRepo.findByEmail(employee.getEmail()) != null) {
//            return "redirect:/employeeProfile";
//        } else if (customerRepo.findByEmail(users.getEmail()) != null){
//            return "redirect:/customerProfile";
//        }
//        return "redirect:/registration";
//
//    }
}
