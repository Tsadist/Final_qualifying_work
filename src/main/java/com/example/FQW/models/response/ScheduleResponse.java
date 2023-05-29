package com.example.FQW.models.response;

import com.example.FQW.models.DB.Schedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
@Builder
public class ScheduleResponse {

    private int numberWeek;
    private HashMap<String, Schedule.ScheduleHours> objDays;

}
