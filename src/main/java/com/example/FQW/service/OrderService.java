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
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final ScheduledRepo scheduledRepo;
    private final VacationRepo vacationRepo;
    private final AdditionServiceRepo additionServiceRepo;
    private final PaymentService paymentService;
    private final UserService userService;
    private final CustomMailSender mailSender;

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
                            .address(orderRequest.getAddress())
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
            order.setAddress(orderRequest.getAddress());
            order.setAdditionServicesId(orderRequest.getAdditionServicesId());

            List<AdditionService> additionServiceList = additionServiceRepo
                    .findAllById(List.of(orderRequest.getAdditionServicesId()));
            calculateOrderDuration(order, additionServiceList);
            costCalculation(order, additionServiceList);
            employeeAppointment(order);

            return getOrder(userDetails, orderId);
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Все параметры запроса нулевые или невалидные");
        }
    }

    public AnswerResponse deleteOrder(CustomUserDetails userDetails, Long orderId) {
        Order order = getOrderIsItExistsFromUserDetails(userDetails, orderId);
        String paymentAnswer = paymentService.delete(order).getMessage();
        orderRepo.delete(order);

        if (orderRepo.findById(orderId).isEmpty()) {
            return new AnswerResponse("Заказ был успешно удален" + paymentAnswer);
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
        order.setOrderStatus(OrderStatus.PAID);
        return paymentURLResponse;
    }

    protected void employeeAppointment(Order order) {
        employeeAppointment(order, null);
    }

    protected void employeeAppointment(Order order, User cleanerRemove) {
        List<Long> cleanerIds = scheduledRepo
                .findAllCleanerFromDayOfWeekAndDuration(order.getId());
        List<User> cleanersFromSchedule = userService.getAllUser(cleanerIds);
        List<User> cleanersFromVacation = vacationRepo
                .findAllCleanerByDateOrder(order.getId());


        cleanersFromSchedule.removeIf(cleanersFromVacation::contains);

        List<Order> orderList = new ArrayList<>();
        Set<User> cleaners;

        cleanersFromSchedule.forEach(cleaner ->
                orderList.addAll(orderRepo
                        .findAllByTheDateAndCleanerId(order.getTheDate(), cleaner.getId())));

        if (orderList.isEmpty()) {
            cleaners = new HashSet<>(cleanersFromSchedule);
        } else {
            cleaners = orderList.stream()
                    .filter(oldOrder -> order.getStartTime() > oldOrder.getStartTime() + oldOrder.getDuration() ||
                            order.getStartTime() + order.getDuration() < oldOrder.getStartTime())
                    .map(Order::getCleaner)
                    .collect(Collectors.toSet());
        }

        if (cleanerRemove != null) {
            cleaners.remove(cleanerRemove);
        }

        if (!cleaners.isEmpty()) {
            User cleaner = cleaners.iterator().next();
            mailSender.send(cleaner.getEmail(), "Новый заказ", String
                    .format("Вам назначен новый заказ. \nДата: %s \nВремя: %dч \nАдрес: %s",
                            order.getTheDate(),
                            order.getStartTime(),
                            order.getAddress()));
            order.setCleaner(cleaner);
            order.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        } else {
            order.setOrderStatus(OrderStatus.NO_EMPLOYEE);
        }

        orderRepo.save(order);
    }

    protected Order getOrder(Long orderId) {
        Supplier<RequestException> requestExceptionSupplier = () -> new RequestException(HttpStatus.FORBIDDEN, "Заказ с таким Id не найден");

        return orderRepo
                .findById(orderId)
                .orElseThrow(requestExceptionSupplier);
    }

    protected OrderResponse getOrderResponse(Order order) {
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
                .address(order.getAddress())
                .additionServicesId(order.getAdditionServicesId())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private List<OrderResponse> getListOrderResponse(List<Order> orderList) {
        return orderList.stream().map(this::getOrderResponse).toList();
    }

    private Order getOrderIsItExistsFromUserDetails(CustomUserDetails userDetails, Long orderId) {
        Order order = getOrder(orderId);
        if (!Objects.equals(order.getCustomer().getId(), userDetails.getClient().getId())
                && !Objects.equals(order.getCleaner().getId(), userDetails.getClient().getId())) {
            throw new RequestException(HttpStatus.LOCKED, "У вас нет доступа к данному заказу");
        }
        return order;
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
