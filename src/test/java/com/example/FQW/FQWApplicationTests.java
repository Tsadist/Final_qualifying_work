package com.example.FQW;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.request.ActivateCodeRequest;
import com.example.FQW.models.request.OrderRequest;
import com.example.FQW.models.request.RegistrationRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.OrderResponse;
import com.example.FQW.repository.UserRepo;
import com.example.FQW.service.OrderService;
import com.example.FQW.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FQWApplicationTests {

    private static User user;

    @Autowired
    private CreateRequests createRequests;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepo userRepo;

    @Test
    void registration() {
        RegistrationRequest registrationRequest = createRequests.getRegistrationRequest();
        AnswerResponse answer = userService.createCustomer(registrationRequest);
        user = userRepo.findByEmail(registrationRequest.getEmail());
        Assertions.assertEquals("Пользователь успешно создан, для входа в ЛК нужно подтвердить аккаунт",
                answer.getMessage());
    }

    @Test
    void activation() {
        ActivateCodeRequest activationCodeRequest = createRequests.getActivationCodeRequest(user.getId());
        AnswerResponse answer = userService.accountActivate(activationCodeRequest);
        Assertions.assertEquals("Аккаунт успешно активирован",
                answer.getMessage());
    }

    @Test
    void createOrder() {
        OrderRequest orderRequest = createRequests.getOrderRequest();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        OrderResponse order = orderService.createOrder(userDetails, orderRequest);
        Assertions.assertEquals(order.getTheDate(), orderRequest.getTheDate());
    }
}
