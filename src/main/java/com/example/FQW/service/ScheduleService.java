package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.Schedule;
import com.example.FQW.models.request.ScheduleRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.ScheduleResponse;
import com.example.FQW.repository.ScheduledRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduledRepo scheduledRepo;
    private final static Short COUNT_WEEK_IN_YEAR = 53;
    private final static Short COUNT_MONTH_WEEK = 4;

    public List<ScheduleResponse> getAllSchedule(CustomUserDetails userDetails) {
        List<Schedule> schedules = scheduledRepo.findAllByCleanerId(userDetails.getClient().getId());
        return getScheduleResponses(schedules);
    }

    public List<ScheduleResponse> createSchedule(CustomUserDetails userDetails, List<ScheduleRequest> scheduleRequests) {
        if (isNumberWeekAndHoursValid(scheduleRequests, COUNT_MONTH_WEEK)) {
            List<Schedule> scheduleList = new ArrayList<>();
            scheduleRequests
                    .forEach(scheduleRequest -> {
                        int numberWeek = scheduleRequest.getNumberWeek();
                        for (; numberWeek <= COUNT_WEEK_IN_YEAR; numberWeek += COUNT_MONTH_WEEK) {
                            Schedule schedule = scheduledRepo.findByCleanerIdAndNumberWeek(
                                    userDetails.getClient().getId(),
                                    numberWeek);

                            if (schedule == null) {
                                schedule = new Schedule();
                                schedule.setCleaner(userDetails.getClient());
                                schedule.setNumberWeek(numberWeek);
                            }

                            schedule.setObjDays(scheduleRequest.getObjDays());
                            scheduleList.add(scheduledRepo.save(schedule));
                        }
                    });
            return getScheduleResponses(scheduleList);
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Номера недель в запросе больше 4-х " +
                    "или меньше 1 или время выходит за пределы 24 часового дня");
        }
    }

    public List<ScheduleResponse> editSchedule(CustomUserDetails userDetails, List<ScheduleRequest> scheduleRequests) {
        if (isNumberWeekAndHoursValid(scheduleRequests, COUNT_WEEK_IN_YEAR)) {
            List<Schedule> scheduleList = new ArrayList<>();
            scheduleRequests.forEach(scheduleRequest -> {
                Schedule schedule = scheduledRepo.findByCleanerIdAndNumberWeek(
                        userDetails.getClient().getId(),
                        scheduleRequest.getNumberWeek());
                schedule.setObjDays(scheduleRequest.getObjDays());
                scheduleList.add(scheduledRepo.save(schedule));
            });
            return getScheduleResponses(scheduleList);
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Номера недель в запросе больше 53-х или меньше 1 " +
                    "или время выходит за пределы 24 часового дня");
        }
    }

    public AnswerResponse deleteAllSchedule(CustomUserDetails userDetails) {
        Long cleanerId = userDetails.getClient().getId();
        scheduledRepo.deleteAllByCleanerId(cleanerId);

        if (scheduledRepo.findAllByCleanerId(cleanerId).isEmpty()) {
            return new AnswerResponse("Расписание сотрудника было успешно удалено");
        } else {
            throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Не удалось удалить расписание");
        }
    }

    private List<ScheduleResponse> getScheduleResponses(List<Schedule> schedules) {
        return schedules
                .stream()
                .map(oneSchedule -> ScheduleResponse.builder()
                        .numberWeek(oneSchedule.getNumberWeek())
                        .objDays(oneSchedule.getObjDays())
                        .build())
                .collect(Collectors.toList());
    }

    private boolean isNumberWeekAndHoursValid(List<ScheduleRequest> scheduleRequests, int countWeek) {
        return scheduleRequests.stream()
                .allMatch(scheduleRequest -> scheduleRequest.getNumberWeek() <= countWeek
                        && scheduleRequest.getNumberWeek() > 0
                        && isTimeValid(scheduleRequest.getObjDays()));
    }

    private boolean isTimeValid(HashMap<String, Schedule.ScheduleHours> objDays) {
        return objDays.values().stream()
                .allMatch(scheduleHours ->
                        scheduleHours.getEndTime() > 0 &&
                                scheduleHours.getEndTime() <= 24 &&
                                scheduleHours.getStartTime() > 0 &&
                                scheduleHours.getStartTime() <= 24);
    }
}
