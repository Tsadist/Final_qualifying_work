package com.example.FQW.models.response;

import com.example.FQW.models.DB.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

@Builder
@Getter
public class ScheduleResponse {

    private int numberWeek;
    private HashMap<String, Schedule.ScheduleHours> objDays;
}
