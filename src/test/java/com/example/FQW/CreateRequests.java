package com.example.FQW;

import com.example.FQW.controller.utils.Randomizer;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.request.ActivateCodeRequest;
import com.example.FQW.models.request.OrderRequest;
import com.example.FQW.models.request.RegistrationRequest;
import com.example.FQW.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateRequests {

    private final UserRepo userRepo;

    public RegistrationRequest getRegistrationRequest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("wasilev@gmail.com");
        request.setPassword("passsss");
        return request;
    }

    public ActivateCodeRequest getActivationCodeRequest(Long customerId) {
        User user = userRepo.findById(customerId).get();
        ActivateCodeRequest request = new ActivateCodeRequest();
        request.setActivationCode(user.getActivationCode());
        return request;
    }

    public OrderRequest getOrderRequest() {
       OrderRequest orderRequest = new OrderRequest();
       orderRequest.setArea(Randomizer.getRandomFloat());
       orderRequest.setAddress(Randomizer.getRandomString());
       orderRequest.setStartTime(Randomizer.getShot());
       orderRequest.setTheDate(Randomizer.getDate());
       orderRequest.setCleaningType(Randomizer.getCleaningType());
       orderRequest.setRoomType(Randomizer.getRoomType());
       orderRequest.setAdditionServicesId(new Long[0]);
       return orderRequest;
    }
}
