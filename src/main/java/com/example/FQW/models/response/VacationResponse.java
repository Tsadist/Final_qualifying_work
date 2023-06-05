package com.example.FQW.models.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Builder
@Getter
public class VacationResponse {

    private Date startDay;
    private Date endDay;
}
