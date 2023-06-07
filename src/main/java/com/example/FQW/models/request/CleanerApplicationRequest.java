package com.example.FQW.models.request;

import lombok.Getter;

@Getter
public class CleanerApplicationRequest {
    private Long orderId;
    private Long cleanerId;
    private String message;
}
