package com.example.FQW.models.request;

import lombok.Getter;

@Getter
public class AuthorizeRequest {

    private String password;
    private String email;
}
