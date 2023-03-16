package com.example.kyrsovay.models.request;

import lombok.Getter;

@Getter
public class RegistrationRequest {

    private String email;
    private String password;
    private String phoneNumber;
}
