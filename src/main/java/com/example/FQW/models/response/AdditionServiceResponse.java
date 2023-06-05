package com.example.FQW.models.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AdditionServiceResponse {

    private String title;
    private int cost;
    private float duration;
}
