package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.DB.Schedule;
import com.example.FQW.models.request.ScheduleRequest;
import com.example.FQW.models.response.ScheduleResponse;
import com.example.FQW.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Secured("CLEANER")
    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleResponse>> getSchedule(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(scheduleService.getAllSchedules(userDetails));
    }

//    @Secured("CLEANER")
//    @PostMapping("/edit/schedule")
//    public ResponseEntity<ScheduleResponse> editSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
//                                                         @RequestBody List<ScheduleRequest> scheduleRequests){
//        return ResponseEntity.ok(scheduleService.editSchedule(userDetails, scheduleRequests));
//    }
}
