package com.example.FQW.models.request;

import com.example.FQW.models.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewEmployeeRequest {

    private String phoneNumber;
    private String email;
    private String name;
    private String surname;
    private UserRole userRole;
}
