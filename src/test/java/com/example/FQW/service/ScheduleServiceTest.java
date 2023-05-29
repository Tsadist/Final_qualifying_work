package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.DB.Schedule;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.request.ScheduleRequest;
import com.example.FQW.models.response.ScheduleResponse;
import com.example.FQW.repository.ScheduledRepo;
import com.example.FQW.repository.UserRepo;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles(profiles = "dev")
public class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ScheduledRepo scheduledRepo;

//    @Test
//    public void isDayLong() {
//        User user = userRepo.findById(1L).get();
//        HashMap<String, int[]> map = new HashMap<>();
//        map.put("5", new int[]{3, 7});
//
//        Schedule schedule = new Schedule();
//        schedule.setCleaner(user);
//        schedule.setNumberWeek(34);
//        schedule.setObjDays(map);
//        Schedule save = scheduledRepo.save(schedule);
//
//        Schedule schedule1 = scheduledRepo.findById(save.getId()).get();
//        HashMap<String, Schedule.ScheduleHours> objDays = schedule1.getObjDays();
//
//        System.out.println(objDays);
//        Assertions.assertFalse(objDays.keySet().isEmpty());
//    }

//    @Test
//    public void editScheduleTest() {
//        User user = userRepo.findById(1L).get();
//        CustomUserDetails userDetails = new CustomUserDetails(user);
//
//        HashMap<String, int[]> map = new HashMap<>();
//        map.put("5", new int[]{3, 7});
//        Schedule schedule = new Schedule();
//        schedule.setCleaner(user);
//        schedule.setNumberWeek(28);
//        schedule.setObjDays(map);
//        Schedule save = scheduledRepo.save(schedule);

//        HashMap<String, int[]> map2 = new HashMap<>();
//        map2.put("1", new int[]{5, 18});
//        Schedule schedule2 = new Schedule();
//        schedule2.setCleaner(user);
//        schedule2.setNumberWeek(33);
//        schedule2.setObjDays(map2);
//        Schedule save2 = scheduledRepo.save(schedule2);

//        System.out.println(map2);

//        ArrayList<Schedule> list = new ArrayList<>();
//        list.add(save);
//        list.add(save2);

//        System.out.println(list.size());

//        editSchedule(userDetails, list);
//    }

    public void editSchedule(CustomUserDetails userDetails, List<Schedule> schedules) {

        for (int i = 1; i < 54; i += schedules.size()) {
//            for (Schedule sch: schedules) {
            Schedule schedule = schedules.get(0);
            Schedule sch = new Schedule();
            sch.setCleaner(schedule.getCleaner());
            sch.setObjDays(schedule.getObjDays());
            sch.setNumberWeek(i);
            scheduledRepo.save(schedule);
//            }
        }
    }

}
