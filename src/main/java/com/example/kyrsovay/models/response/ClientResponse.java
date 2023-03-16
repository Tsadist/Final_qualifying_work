package com.example.kyrsovay.models.response;

import com.example.kyrsovay.models.enums.ClientRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientResponse {

    private Long id;
    private String phoneNumber;
    private String email;
    private ClientRole role;
}
