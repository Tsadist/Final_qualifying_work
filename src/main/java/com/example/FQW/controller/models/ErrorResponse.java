package com.example.FQW.controller.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private String errorPath;
}
