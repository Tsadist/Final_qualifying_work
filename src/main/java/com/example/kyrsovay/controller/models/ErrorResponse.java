package com.example.kyrsovay.controller.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private String errorPath;
}
