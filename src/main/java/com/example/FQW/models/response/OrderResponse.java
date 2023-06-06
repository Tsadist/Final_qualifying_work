package com.example.FQW.models.response;

import com.example.FQW.models.enums.CleaningType;
import com.example.FQW.models.enums.OrderStatus;
import com.example.FQW.models.enums.RoomType;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Builder
@Getter
public class OrderResponse {

    private Long id;
    private float area;
    private RoomType roomType;
    private CleaningType cleaningType;
    private Date theDate;
    private Short startTime;
    private CleanerResponse cleaner;
    private Integer cost;
    private Float duration;
    private Long[] additionServicesId;
    private String address;
    private OrderStatus orderStatus;
}
