package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VacationController {

//    private final VacationService vacationService;
//
//    @PreAuthorize("hasRole('MANAGER') or hasRole('CLEANER')")
//    @GetMapping("/vacation")
//    public ResponseEntity<OrderResponse> ge(@PathVariable Long orderId,
//                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
//        return ResponseEntity.ok(vacationService.getOrder(userDetails, orderId));
//    }
}
