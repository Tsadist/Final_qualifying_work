package com.example.FQW.models.response;

import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.ChatStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ChatResponse {

    private String topic;
    private ChatStatus status;
    private LocalDateTime createTime;
    private LocalDateTime lastModifiedTime;
    private User createUser;
}
