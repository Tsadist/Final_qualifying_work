package com.example.FQW.models.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CleanerResponse {

    private String phoneNumber;
    private String name;
    private String surname;
}
