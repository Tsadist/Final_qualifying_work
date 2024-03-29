package com.example.FQW.service;

import com.example.FQW.models.DB.AdditionService;
import com.example.FQW.models.request.AdditionServiceRequest;
import com.example.FQW.models.response.AdditionServiceResponse;
import com.example.FQW.repository.AdditionServiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdditionServiceService {

    private final AdditionServiceRepo additionServiceRepo;

    public List<AdditionServiceResponse> getAdditionService() {
        List<AdditionService> additionServiceList = additionServiceRepo.findAll();
        return getAdditionServiceResponseList(additionServiceList);
    }

    public List<AdditionServiceResponse> createAdditionService(List<AdditionServiceRequest> additionServiceRequest) {
        List<AdditionService> additionServiceList = new ArrayList<>();
        additionServiceRequest.forEach(ASRequest -> additionServiceList
                .add(additionServiceRepo
                        .save(AdditionService
                                .builder()
                                .cost(ASRequest.getCost())
                                .duration(ASRequest.getDuration())
                                .title(ASRequest.getTitle())
                                .build())));
        return getAdditionServiceResponseList(additionServiceList);
    }

    private List<AdditionServiceResponse> getAdditionServiceResponseList(List<AdditionService> additionServiceList) {
        return additionServiceList
                .stream()
                .map(additionService -> AdditionServiceResponse
                        .builder()
                        .id(additionService.getId())
                        .title(additionService.getTitle())
                        .cost(additionService.getCost())
                        .duration(additionService.getDuration())
                        .build())
                .collect(Collectors.toList());
    }
}
