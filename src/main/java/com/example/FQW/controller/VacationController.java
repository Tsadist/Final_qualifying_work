package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.request.VacationRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.VacationResponse;
import com.example.FQW.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/vacation/{cleanerId}")
    public ResponseEntity<List<VacationResponse>> getVacation(@PathVariable Long cleanerId) {
        return ResponseEntity.ok(vacationService.getVacation(cleanerId));
    }

    @PreAuthorize("hasRole('CLEANER')")
    @GetMapping("/vacation")
    public ResponseEntity<List<VacationResponse>> getVacation(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(vacationService.getVacation(userDetails));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/vacation/{cleanerId}/create")
    public ResponseEntity<VacationResponse> createVacation(@PathVariable Long cleanerId,
                                                                 @RequestBody VacationRequest vacationRequest,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(vacationService.createVacation(userDetails, cleanerId, vacationRequest));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/vacation/{vacationId}/delete")
    public ResponseEntity<AnswerResponse> createVacation (@PathVariable Long vacationId){
        return ResponseEntity.ok(vacationService.deleteVacation(vacationId));
    }

}
