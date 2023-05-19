package com.example.FQW.models.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CleanerResponse {

    private String phoneNumber;
    private String name;
    private String surname;
}
