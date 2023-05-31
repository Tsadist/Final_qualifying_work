package com.example.FQW.controller;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.request.ChatRequest;
import com.example.FQW.models.request.ChatStatusRequest;
import com.example.FQW.models.request.MessageRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.ChatResponse;
import com.example.FQW.models.response.MessageResponse;
import com.example.FQW.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PreAuthorize("hasRole('MODERATOR') or hasRole('CUSTOMER')")
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<ChatResponse> getChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getChat(chatId));
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('CUSTOMER')")
    @GetMapping("/chats")
    public ResponseEntity<List<ChatResponse>> getAllChat(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(chatService.getAllChat(userDetails));
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('CUSTOMER')")
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<MessageResponse>> getAllMessage(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getAllMessage(chatId));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/chat/create")
    public ResponseEntity<ChatResponse> createChat(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestBody ChatRequest chatRequest) {
        return ResponseEntity.ok(chatService.createChat(userDetails, chatRequest));
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('CUSTOMER')")
    @GetMapping("/message/create")
    public ResponseEntity<MessageResponse> createMessage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(chatService.createMessage(userDetails, messageRequest));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/chat/{chatId}/delete")
    public ResponseEntity<AnswerResponse> deleteChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.deleteChat(chatId));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/chat/{chatId}/status/change")
    public ResponseEntity<ChatResponse> changeStatusChat(@PathVariable Long chatId,
                                                         @RequestBody ChatStatusRequest chatStatusRequest) {
        return ResponseEntity.ok(chatService.changeStatusChat(chatId, chatStatusRequest));
    }


}
