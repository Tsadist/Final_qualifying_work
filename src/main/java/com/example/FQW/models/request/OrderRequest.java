package com.example.FQW.models.request;

import com.example.FQW.models.enums.CleaningType;
import com.example.FQW.models.enums.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class OrderRequest {

    private Float area;
    private RoomType roomType;
    private CleaningType cleaningType;
    private Date theDate;
    private Short startTime;
    private String address;
    private Long[] additionServicesId;
}
