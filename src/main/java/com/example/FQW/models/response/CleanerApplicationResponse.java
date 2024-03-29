package com.example.FQW.models.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CleanerApplicationResponse {

    private String nameCleaner;
    private String surnameCleaner;
    private String numberPhoneCleaner;
    private String message;
    private OrderResponse order;

}
