package com.example.FQW.models.request;

import com.example.FQW.models.DB.Schedule;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class ScheduleRequest {

    private int numberWeek;
    private HashMap<String, Schedule.ScheduleHours> objDays;
}
