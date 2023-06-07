package com.example.FQW.models.request;

import com.example.FQW.models.DB.CleanersApplication;
import lombok.Getter;

@Getter
public class ApplicationStatusRequest {

    private CleanersApplication.Status status;
}
