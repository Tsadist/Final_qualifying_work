package com.example.kyrsovay.models.response;

import com.example.kyrsovay.models.enums.CleaningType;
import com.example.kyrsovay.models.enums.OrderStatus;
import com.example.kyrsovay.models.enums.RoomType;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Getter
@Builder
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
    private OrderStatus orderStatus;

}
