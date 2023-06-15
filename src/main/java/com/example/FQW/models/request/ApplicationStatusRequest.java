package com.example.FQW.models.request;

import com.example.FQW.models.DB.CleanersApplication;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatusRequest {

    private CleanersApplication.Status status;
}
