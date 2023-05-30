package com.example.FQW.service;

import com.example.FQW.models.DB.AdditionService;
import com.example.FQW.models.response.AdditionServiceResponse;
import com.example.FQW.repository.AdditionServiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdditionServiceService {

    private final AdditionServiceRepo additionServiceRepo;

    public List<AdditionServiceResponse> getAdditionService() {
        List<AdditionService> additionServiceList = additionServiceRepo.findAll();

        return additionServiceList
                .stream()
                .map(additionService -> AdditionServiceResponse
                        .builder()
                        .title(additionService.getTitle())
                        .cost(additionService.getCost())
                        .duration(additionService.getDuration())
                        .build())
                .collect(Collectors.toList());
    }
}
