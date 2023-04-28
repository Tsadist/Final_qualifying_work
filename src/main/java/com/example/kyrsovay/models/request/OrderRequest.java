package com.example.kyrsovay.models.request;

import com.example.kyrsovay.models.enums.CleaningType;
import com.example.kyrsovay.models.enums.RoomType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.sql.Date;

@Getter
public class OrderRequest {

    private Float area;
    private RoomType roomType;
    private CleaningType cleaningType;

//    @JsonFormat(pattern="dd-MM-yyyy")
    private Date theDate;
    private Short startTime;
}
