package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.AdditionService;
import com.example.FQW.models.DB.Order;
import com.example.FQW.models.DB.Payment;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.CleaningType;
import com.example.FQW.models.enums.OrderStatus;
import com.example.FQW.models.enums.RoomType;
import com.example.FQW.models.request.OrderRequest;
import com.example.FQW.models.response.AnswerResponse;
import com.example.FQW.models.response.CleanerResponse;
import com.example.FQW.models.response.OrderResponse;
import com.example.FQW.models.response.PaymentURLResponse;
import com.example.FQW.repository.AdditionServiceRepo;
import com.example.FQW.repository.OrderRepo;
import com.example.FQW.repository.ScheduledRepo;
import com.example.FQW.repository.VacationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final ScheduledRepo scheduledRepo;
    private final VacationRepo vacationRepo;
    private final AdditionServiceRepo additionServiceRepo;
    private final PaymentService paymentService;

    public OrderResponse getOrder(CustomUserDetails userDetails, Long orderId) {
        Order order = getOrderIsItExistsFromUserDetails(userDetails, orderId);
        return getOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders(CustomUserDetails userDetails) {
        User user = userDetails.getClient();
        List<Order> orderList;
        switch (user.getUserRole()) {
            case CUSTOMER -> orderList = orderRepo.findAllByCustomerId(user.getId());
            case CLEANER -> orderList = orderRepo.findAllByCleanerId(user.getId());
            default -> throw new RequestException(HttpStatus.UNAUTHORIZED, "У вас нет доступа к этому методу");
        }
        return getListOrderResponse(orderList);
    }

    public OrderResponse createOrder(CustomUserDetails userDetails, OrderRequest orderRequest) {
        if (isCreateOrder(orderRequest)) {
            Order newOrder = orderRepo
                    .save(Order
                            .builder()
                            .customer(userDetails.getClient())
                            .area(orderRequest.getArea())
                            .roomType(orderRequest.getRoomType())
                            .cleaningType(orderRequest.getCleaningType())
                            .theDate(orderRequest.getTheDate())
                            .startTime(orderRequest.getStartTime())
                            .additionServicesId(orderRequest.getAdditionServicesId())
                            .build());
            List<AdditionService> additionServiceList = additionServiceRepo
                    .findAllById(List.of(orderRequest.getAdditionServicesId()));
            calculateOrderDuration(newOrder, additionServiceList);
            employeeAppointment(newOrder);
            costCalculation(newOrder, additionServiceList);
            return getOrderResponse(newOrder);
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Какой-то из параметров запроса нулевой или невалидный");
        }
    }

    public OrderResponse editOrder(CustomUserDetails userDetails, Long orderId, OrderRequest orderRequest) {
        Order order = getOrderIsItExistsFromUserDetails(userDetails, orderId);

        if (isEditOrder(orderRequest)) {
            order.setCleaningType(orderRequest.getCleaningType());
            order.setArea(orderRequest.getArea());
            order.setRoomType(orderRequest.getRoomType());
            order.setTheDate(orderRequest.getTheDate());
            order.setStartTime(orderRequest.getStartTime());
            order.setAdditionServicesId(orderRequest.getAdditionServicesId());

            return getOrderResponse(orderRepo.save(order));
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Все параметры запроса нулевые или невалидные");
        }
    }

    public AnswerResponse deleteOrder(CustomUserDetails userDetails, Long orderId) {
        Order order = getOrderIsItExistsFromUserDetails(userDetails, orderId);
        orderRepo.delete(order);

        if (orderRepo.findById(orderId).isEmpty()) {
            return new AnswerResponse("Заказ был успешно удален");
        } else {
            throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Не удалось удалить заказ");
        }
    }

    public PaymentURLResponse getPaymentURL(Long orderId, CustomUserDetails userDetails) {
        Order order = getOrderIsItExistsFromUserDetails(userDetails, orderId);
        Payment payment = paymentService.getPaymentForOrderId(order);
        if (payment == null) {
            payment = paymentService.createPayment(order);
        }
        PaymentURLResponse paymentURLResponse = new PaymentURLResponse();
        paymentURLResponse.setPaymentURL(payment.getLinkForPayment());
        return paymentURLResponse;
    }

    private OrderResponse getOrderResponse(Order order) {
        User cleaner = order.getCleaner();

        if (cleaner == null) {
            cleaner = new User();
        }

        return OrderResponse.builder()
                .id(order.getId())
                .area(order.getArea())
                .roomType(order.getRoomType())
                .cleaningType(order.getCleaningType())
                .theDate(order.getTheDate())
                .startTime(order.getStartTime())
                .cleaner(CleanerResponse.builder()
                        .name(cleaner.getName())
                        .surname(cleaner.getSurname())
                        .phoneNumber(cleaner.getPhoneNumber())
                        .build())
                .cost(order.getCost())
                .duration(order.getDuration())
                .additionServicesId(order.getAdditionServicesId())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private List<OrderResponse> getListOrderResponse(List<Order> orderList) {
        return orderList.stream().map(this::getOrderResponse).toList();
    }

    private void employeeAppointment(Order order) {
        List<User> cleanersFromSchedule = scheduledRepo
                .findAllCleanerFromDayOfWeekAndDuration(order.getId());
        List<User> cleanersFromVacation = vacationRepo
                .findAllCleanerByDateOrder(order.getId());

        List<User> suitableCleaner = cleanersFromSchedule.stream()
                .filter(cleaner -> !cleanersFromVacation.contains(cleaner))
                .toList();



        if (!suitableCleaner.isEmpty()) {
            order.setCleaner(suitableCleaner.get(0));
            order.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        } else {
            order.setOrderStatus(OrderStatus.NO_EMPLOYEE);
        }

        orderRepo.save(order);
    }

    private void calculateOrderDuration(Order order, List<AdditionService> additionServiceList) {
        float minTime = (float) (Math.ceil(order.getArea() / 25f) * 0.5f);
        RoomType roomType = order.getRoomType();
        float duration = switch (order.getCleaningType()) {
            case REGULAR -> minTime * roomType.getRegular();
            case GENERAL -> minTime * roomType.getGeneral();
            case AFTER_REPAIR -> minTime * roomType.getAfterRepair();
        };

        List<Float> durationAdditionService = additionServiceList
                .stream()
                .map(AdditionService::getDuration)
                .toList();
        for (Float durationAS : durationAdditionService) {
            duration += durationAS;
        }

        order.setDuration(duration);
        orderRepo.save(order);

    }

    private Order getOrderIsItExistsFromUserDetails(CustomUserDetails userDetails, Long orderId) {
        Supplier<RequestException> requestExceptionSupplier = () -> new RequestException(HttpStatus.FORBIDDEN, "Заказ с таким Id не найден");

        Order order = orderRepo
                .findById(orderId)
                .orElseThrow(requestExceptionSupplier);

        if (order.getCustomer() != userDetails.getClient() || order.getCleaner() != userDetails.getClient()) {
            throw requestExceptionSupplier.get();
        }
        return order;
    }

    private void costCalculation(Order order, List<AdditionService> additionServiceList) {
        Integer cost = (int) (order.getDuration() / 0.5F * 1000);
        List<Integer> costAdditionService = additionServiceList
                .stream()
                .map(AdditionService::getCost)
                .toList();

        for (Integer costAS : costAdditionService) {
            cost += costAS;
        }

        order.setCost(cost);
        orderRepo.save(order);
    }

    private boolean isCreateOrder(OrderRequest orderRequest) {
        return (isCorrectArea(orderRequest.getArea()) &&
                isCorrectRoomType(orderRequest.getRoomType()) &&
                isCorrectTheDate(orderRequest.getTheDate()) &&
                isCorrectCleaningType(orderRequest.getCleaningType()) &&
                isCorrectStartTime(orderRequest.getStartTime())) &&
                isCorrectAdditionServices(orderRequest.getAdditionServicesId());

    }

    private boolean isEditOrder(OrderRequest orderRequest) {
        Float area = orderRequest.getArea();
        RoomType roomType = orderRequest.getRoomType();
        Date theDate = orderRequest.getTheDate();
        CleaningType cleaningType = orderRequest.getCleaningType();
        Short startTime = orderRequest.getStartTime();
        Long[] additionServicesId = orderRequest.getAdditionServicesId();

        return ((area == null || isCorrectArea(area)) &&
                (roomType == null || isCorrectRoomType(roomType)) &&
                (theDate == null || isCorrectTheDate(theDate)) &&
                (cleaningType == null || isCorrectCleaningType(cleaningType)) &&
                (startTime == null || isCorrectStartTime(startTime))) &&
                (additionServicesId == null || isCorrectAdditionServices(additionServicesId));
    }

    private boolean isCorrectTheDate(Date theDate) {
        return (theDate != null &&
                !theDate.before(new java.util.Date()));
    }

    private boolean isCorrectStartTime(Short startTime) {
        return (startTime != null &&
                startTime >= 8 && startTime <= 22);
    }

    private boolean isCorrectArea(Float area) {
        return (area != null &&
                area > 10);
    }

    private boolean isCorrectCleaningType(CleaningType cleaningType) {
        return (cleaningType != null &&
                Arrays.toString(CleaningType.values()).contains(cleaningType.toString()));
    }

    private boolean isCorrectRoomType(RoomType roomType) {
        return (roomType != null &&
                Arrays.toString(RoomType.values()).contains(roomType.toString()));
    }

    private boolean isCorrectAdditionServices(Long[] additionServicesId) {
        if (additionServicesId.length == 0) {
            return true;
        }
        List<AdditionService> allAdditionServices = additionServiceRepo.findAll();
        List<Long> additionServiceId = allAdditionServices
                .stream()
                .map(AdditionService::getId)
                .toList();
        return additionServiceId.containsAll(List.of(additionServicesId));
    }
}
