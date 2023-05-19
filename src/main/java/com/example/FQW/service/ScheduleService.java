package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.DB.Schedule;
import com.example.FQW.models.request.ScheduleRequest;
import com.example.FQW.models.response.ScheduleResponse;
import com.example.FQW.repository.ScheduledRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduledRepo scheduledRepo;
    private final static Short countWeekInYear = 53;

    public List<ScheduleResponse> getAllSchedules(CustomUserDetails userDetails) {
        List<Schedule> schedules = scheduledRepo.getAllByCleanerId(userDetails.getClient().getId());
        return schedules
                .stream()
                .map(this::getScheduleResponse)
                .collect(Collectors.toList());
    }

    private ScheduleResponse getScheduleResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .dayOfWeek(schedule.getDayOfWeek())
                .hours(schedule.getHours())
                .numberWeek(schedule.getNumberWeek())
                .build();
    }

//    public List<ScheduleResponse> editSchedule(CustomUserDetails userDetails, List<ScheduleRequest> scheduleRequests) {
//        int scheduleLength = scheduleRequests.size();
//        for (int i = 0; i < countWeekInYear; i++) {
//
//        }
//    }
}
