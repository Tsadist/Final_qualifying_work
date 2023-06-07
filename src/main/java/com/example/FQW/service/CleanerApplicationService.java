package com.example.FQW.service;

import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.CleanersApplication;
import com.example.FQW.models.DB.Order;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.request.ApplicationStatusRequest;
import com.example.FQW.models.request.CleanerApplicationRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.CleanerApplicationResponse;
import com.example.FQW.repository.CleanerApplicationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CleanerApplicationService {

    private final CleanerApplicationRepo applicationRepo;
    private final UserService userService;
    private final OrderService orderService;
    private final MailSender mailSender;

    public List<CleanerApplicationResponse> getAllApplication() {
        List<CleanersApplication> applications = applicationRepo.findAllByStatus(CleanersApplication.Status.CREATED);
        return applications.stream().map(this::getCleanerApplicationResponse).toList();
    }

    public CleanerApplicationResponse createApplication(CleanerApplicationRequest applicationRequest) {
        return getCleanerApplicationResponse(applicationRepo.save(CleanersApplication
                .builder()
                .cleanerId(applicationRequest.getCleanerId())
                .orderId(applicationRequest.getOrderId())
                .message(applicationRequest.getMessage())
                .status(CleanersApplication.Status.CREATED)
                .build()));
    }

    public AnswerResponse editStatusApplication(Long applicationId, ApplicationStatusRequest applicationStatusRequest) {
        CleanersApplication application = getCleanerApplication(applicationId);
        switch (application.getStatus()) {
            case OK, REJECTED -> throw new RequestException(HttpStatus.NOT_FOUND, "Данная заявка уже обработана");
        }

        application.setStatus(applicationStatusRequest.getStatus());
        Order order = orderService.getOrder(application.getOrderId());
        User cleaner = userService.getUser(application.getCleanerId());
        switch (application.getStatus()) {
            case OK -> {
                orderService.employeeAppointment(order, cleaner);
                return new AnswerResponse("Статус заявки был изменен на OK");
            }
            case REJECTED -> {
                mailSender.send(cleaner.getEmail(),
                        "Заявка на отказ от заказа",
                        String.format("Заявка на отказ от заказа № %d отклонен", order.getId()));
                return new AnswerResponse("Статус заявки был изменен на REJECTED");
            }
                default -> throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Статус заявки не был изменен");
        }
    }

    private CleanersApplication getCleanerApplication(Long applicationId) {
        Supplier<RequestException> requestExceptionSupplier = () -> new RequestException(HttpStatus.FORBIDDEN, "Заявка с таким Id не найдена");
        return applicationRepo.findById(applicationId).orElseThrow(requestExceptionSupplier);
    }

    private CleanerApplicationResponse getCleanerApplicationResponse(CleanersApplication application) {
        User cleaner = userService.getUser(application.getCleanerId());
        Order order = orderService.getOrder(application.getOrderId());

        return CleanerApplicationResponse
                .builder()
                .nameCleaner(cleaner.getName())
                .surnameCleaner(cleaner.getSurname())
                .numberPhoneCleaner(cleaner.getPhoneNumber())
                .orderId(order.getId())
                .durationOrder(order.getDuration())
                .build();
    }
}
