package com.example.FQW.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ScheduleResponse {

    private int dayOfWeek;
    private int[] hours;
    private int numberWeek;

}
