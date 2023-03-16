package com.example.kyrsovay.models.request;

import com.example.kyrsovay.models.enums.CleaningType;
import com.example.kyrsovay.models.enums.RoomType;
import lombok.Getter;

import java.sql.Date;

@Getter
public class OrderRequest {

    private Float area;
    private RoomType roomType;
    private CleaningType cleaningType;
    private Date theDate;
    private Short startTime;
}
