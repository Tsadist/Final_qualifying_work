package com.example.FQW.models.request;

import com.example.FQW.models.enums.ChatStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatStatusRequest {

    private ChatStatus chatStatus;
}
