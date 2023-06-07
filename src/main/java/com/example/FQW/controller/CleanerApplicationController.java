package com.example.FQW.controller;

import com.example.FQW.models.request.ApplicationStatusRequest;
import com.example.FQW.models.request.CleanerApplicationRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.CleanerApplicationResponse;
import com.example.FQW.service.CleanerApplicationService;
import jakarta.validation.constraints.DecimalMax;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CleanerApplicationController {

    private final CleanerApplicationService cleanerApplicationService;

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/applications")
    public ResponseEntity<List<CleanerApplicationResponse>> getAllApplication() {
        return ResponseEntity.ok(cleanerApplicationService.getAllApplication());
    }

    @PreAuthorize("hasRole('CLEANER')")
    @PostMapping("/application/create")
    public ResponseEntity<CleanerApplicationResponse> createApplication(
            @RequestBody CleanerApplicationRequest cleanerApplicationRequest) {
        return ResponseEntity.ok(cleanerApplicationService.createApplication(cleanerApplicationRequest));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/application/{applicationId}/new_status")
    public ResponseEntity<AnswerResponse> editApplication(@PathVariable Long applicationId,
                                                          @RequestBody ApplicationStatusRequest applicationStatusRequest) {
        return ResponseEntity.ok(cleanerApplicationService.editStatusApplication(applicationId, applicationStatusRequest));
    }

}
