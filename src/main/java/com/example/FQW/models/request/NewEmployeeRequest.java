package com.example.FQW.models.request;

import com.example.FQW.models.enums.UserRole;
import lombok.Getter;

@Getter
public class NewEmployeeRequest {

    private String phoneNumber;
    private String email;
    private String name;
    private String surname;
    private UserRole userRole;
}
