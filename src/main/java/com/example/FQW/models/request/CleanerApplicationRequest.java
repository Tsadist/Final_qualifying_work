package com.example.FQW.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CleanerApplicationRequest {
    private Long orderId;
    private Long cleanerId;
    private String message;
}
