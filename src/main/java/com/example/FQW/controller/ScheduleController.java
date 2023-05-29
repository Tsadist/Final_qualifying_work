package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.request.ScheduleRequest;
import com.example.FQW.models.response.MessageResponse;
import com.example.FQW.models.response.ScheduleResponse;
import com.example.FQW.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PreAuthorize("hasRole('CLEANER')")
    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleResponse>> getSchedule(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(scheduleService.getAllSchedule(userDetails));
    }

    @PreAuthorize("hasRole('CLEANER')")
    @PostMapping("/create/schedule")
    public ResponseEntity<List<ScheduleResponse>> createSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @RequestBody List<ScheduleRequest> scheduleRequests) {
        return ResponseEntity.ok(scheduleService.createSchedule(userDetails, scheduleRequests));
    }

    @PreAuthorize("hasRole('CLEANER')")
    @PostMapping("/edit/schedule")
    public ResponseEntity<List<ScheduleResponse>> editSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @RequestBody List<ScheduleRequest> scheduleRequests) {
        return ResponseEntity.ok(scheduleService.editSchedule(userDetails, scheduleRequests));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/delete/all/schedule")
    public ResponseEntity<MessageResponse> deleteOrder(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(scheduleService.deleteAllSchedule(userDetails));
    }
}
