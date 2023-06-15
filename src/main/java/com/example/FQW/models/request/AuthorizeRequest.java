package com.example.FQW.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizeRequest {

    private String password;
    private String email;
}
