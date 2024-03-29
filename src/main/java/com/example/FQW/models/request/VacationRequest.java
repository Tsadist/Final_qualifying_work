package com.example.FQW.models.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class VacationRequest {

    private Date startDay;
    private Date endDay;
    private Long vacationId;
    private Long cleanerId;
}
