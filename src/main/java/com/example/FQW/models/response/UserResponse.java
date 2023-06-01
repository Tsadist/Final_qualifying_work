package com.example.FQW.models.response;

import com.example.FQW.models.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String phoneNumber;
    private String name;
    private String surname;
    private String email;
    private UserRole role;
}
