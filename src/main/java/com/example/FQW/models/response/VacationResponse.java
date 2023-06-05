package com.example.FQW.models.response;

import lombok.Builder;

import java.sql.Date;

@Builder
public class VacationResponse {

    private Date startDay;
    private Date endDay;
}
