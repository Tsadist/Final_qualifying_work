package com.example.FQW.models.response;

import com.example.FQW.models.DB.Chat;
import com.example.FQW.models.DB.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MessageResponse {

    private LocalDateTime time;
    private String text;
    private String name;
    private String surname;
    private Long userId;
    private Long chatId;
}
