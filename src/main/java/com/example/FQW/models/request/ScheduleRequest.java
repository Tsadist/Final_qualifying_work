package com.example.FQW.models.request;

import lombok.Getter;

@Getter
public class ScheduleRequest {

    private Short dayOfWeek;
    private int[] hours;
    private int numberWeek;

}
