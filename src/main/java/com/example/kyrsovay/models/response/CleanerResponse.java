package com.example.kyrsovay.models.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CleanerResponse {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
}
