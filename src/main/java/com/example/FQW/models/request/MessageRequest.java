package com.example.FQW.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

    private String text;
    private Long chatId;
}
