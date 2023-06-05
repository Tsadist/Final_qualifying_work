package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.Chat;
import com.example.FQW.models.DB.Message;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.ChatStatus;
import com.example.FQW.models.enums.UserRole;
import com.example.FQW.models.request.ChatRequest;
import com.example.FQW.models.request.ChatStatusRequest;
import com.example.FQW.models.request.MessageRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.ChatResponse;
import com.example.FQW.models.response.MessageResponse;
import com.example.FQW.repository.ChatRepo;
import com.example.FQW.repository.MessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;

    public ChatResponse getChat(Long chatId) {
        return getChatResponse(getChatIfHeExist(chatId));
    }

    public List<MessageResponse> getAllMessage(Long chatId) {
        Chat chat = getChatIfHeExist(chatId);
        return getListMessageResponse(messageRepo.findAllByChatId(chat.getId()));
    }

    public List<ChatResponse> getAllChat(CustomUserDetails userDetails) {
        User user = userDetails.getClient();
        if (user.getUserRole() == UserRole.CUSTOMER) {
            return getListChatResponse(chatRepo.findAllByCreateUserId(user.getId()));
        } else {
            return getListChatResponse(chatRepo.findAll());
        }
    }

    public MessageResponse createMessage(CustomUserDetails userDetails, MessageRequest messageRequest) {
        Chat chat = getChatIfHeExist(messageRequest.getChatId());
        chat.setLastModifiedTime(LocalDateTime.now());
        chatRepo.save(chat);
        return getMessageResponse(messageRepo
                .save(Message
                        .builder()
                        .time(LocalDateTime.now())
                        .text(messageRequest.getText())
                        .user(userDetails.getClient())
                        .chat(chat)
                        .build()));
    }

    public ChatResponse createChat(CustomUserDetails userDetails, ChatRequest chatRequest) {

        return getChatResponse(chatRepo
                .save(Chat
                        .builder()
                        .status(ChatStatus.CREATED)
                        .topic(chatRequest.getTopic())
                        .createUser(userDetails.getClient())
                        .createTime(LocalDateTime.now())
                        .lastModifiedTime(LocalDateTime.now())
                        .build()));
    }

    public AnswerResponse deleteChat(Long chatId) {
        Chat chat = getChatIfHeExist(chatId);
        if (chat.getStatus() == ChatStatus.CLOSED) {
            chatRepo.delete(chat);
            messageRepo.deleteAllByChatId(chatId);
            if (chatRepo.findById(chatId).isEmpty() && messageRepo.findAllByChatId(chatId).isEmpty()) {
                return new AnswerResponse("Чат был успешно удален");
            } else {
                throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Не удалось удалить чат");
            }
        } else {
            throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Данный чат не закрыт");
        }

    }

    public ChatResponse changeStatusChat(Long chatId, ChatStatusRequest chatStatusRequest) {
        if (Arrays.toString(ChatStatus.values())
                .contains(chatStatusRequest.getChatStatus().toString())) {
            Chat chat = getChatIfHeExist(chatId);
            chat.setStatus(chatStatusRequest.getChatStatus());
            return getChatResponse(chatRepo.save(chat));
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Данный статус не поддерживается");
        }
    }

    private MessageResponse getMessageResponse(Message message) {
        return MessageResponse
                .builder()
                .time(message.getTime())
                .text(message.getText())
                .userId(message.getUser())
                .chatId(message.getChat())
                .build();
    }

    private List<MessageResponse> getListMessageResponse(List<Message> messageList) {
        return messageList
                .stream()
                .map(this::getMessageResponse)
                .collect(Collectors.toList());
    }

    private List<ChatResponse> getListChatResponse(List<Chat> chatList) {
        return chatList
                .stream()
                .map(this::getChatResponse)
                .collect(Collectors.toList());
    }

    private ChatResponse getChatResponse(Chat chat) {
        return ChatResponse
                .builder()
                .status(chat.getStatus())
                .topic(chat.getTopic())
                .createUser(chat.getCreateUser())
                .createTime(chat.getCreateTime())
                .lastModifiedTime(chat.getLastModifiedTime())
                .build();
    }

    private Chat getChatIfHeExist(Long chatId) {
        Supplier<RequestException> requestExceptionSupplier = () -> new RequestException(HttpStatus.FORBIDDEN, "Чат с таким Id не найден");

        return chatRepo
                .findById(chatId)
                .orElseThrow(requestExceptionSupplier);
    }
}
