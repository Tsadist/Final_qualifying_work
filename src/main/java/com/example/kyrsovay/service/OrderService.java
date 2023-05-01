package com.example.kyrsovay.service;

import com.example.kyrsovay.config.ClientUserDetails;
import com.example.kyrsovay.ex.RequestException;
import com.example.kyrsovay.models.DB.Cleaner;
import com.example.kyrsovay.models.DB.Client;
import com.example.kyrsovay.models.DB.Order;
import com.example.kyrsovay.models.DB.Schedule;
import com.example.kyrsovay.models.enums.CleaningType;
import com.example.kyrsovay.models.enums.OrderStatus;
import com.example.kyrsovay.models.enums.RoomType;
import com.example.kyrsovay.models.request.OrderRequest;
import com.example.kyrsovay.models.response.CleanerResponse;
import com.example.kyrsovay.models.response.MessageResponse;
import com.example.kyrsovay.models.response.OrderResponse;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.repository.ScheduledRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final ScheduledRepo scheduledRepo;
    private static Client client = new Client();

    public OrderResponse getOrder(ClientUserDetails userDetails, Long orderId) {
        Order order = returnOrderIsItExistsFromUserDetails(userDetails, orderId);
        return getOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders(ClientUserDetails userDetails) {
        List<Order> orderList = orderRepo.findAllByCustomerId(userDetails.getClient().getId());
        return orderList
                .stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse createOrder(ClientUserDetails userDetails, OrderRequest orderRequest) {
        if (isCreateOrder(orderRequest)){
            Order order = new Order();
            order.setCustomer(userDetails.getClient());
            order.setArea(orderRequest.getArea());
            order.setRoomType(orderRequest.getRoomType());
            order.setCleaningType(orderRequest.getCleaningType());
            order.setTheDate(orderRequest.getTheDate());
            order.setStartTime(orderRequest.getStartTime());

            Order newOrder = orderRepo.save(order);
            calculateOrderDuration(newOrder);
            employeeAppointment(newOrder);
            costCalculation(newOrder);

            return getOrderResponse(newOrder);
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Какой-то из параметров запроса нулевой или невалидный");
        }

    }

    public OrderResponse editOrder(ClientUserDetails userDetails, Long orderId, OrderRequest orderRequest){
        Order order = returnOrderIsItExistsFromUserDetails(userDetails, orderId);

        if (isEditOrder(orderRequest)){
            order.setCleaningType(orderRequest.getCleaningType());
            order.setArea(orderRequest.getArea());
            order.setRoomType(orderRequest.getRoomType());
            order.setTheDate(orderRequest.getTheDate());
            order.setStartTime(orderRequest.getStartTime());

            return getOrderResponse(orderRepo.save(order));
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Все параметры запроса нулевые или невалидные");
        }
    }

    public MessageResponse deleteOrder(ClientUserDetails userDetails, Long orderId) {
        Order order = returnOrderIsItExistsFromUserDetails(userDetails, orderId);
        orderRepo.delete(order);

        if (orderRepo.findById(orderId).isEmpty()){
            return new MessageResponse("Заказ был успешно удален");
        } else {
            throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Не удалось удалить заказ");
        }
    }

    private OrderResponse getOrderResponse(Order order){
        Cleaner cleaner = order.getCleaner();

        if (cleaner != null){
            client = cleaner.getClient();
        } else {
            cleaner = new Cleaner();
        }

        return OrderResponse.builder()
                .id(order.getId())
                .area(order.getArea())
                .roomType(order.getRoomType())
                .cleaningType(order.getCleaningType())
                .theDate(order.getTheDate())
                .startTime(order.getStartTime())
                .cleaner(CleanerResponse.builder()
                        .id(cleaner.getId())
                        .name(cleaner.getName())
                        .surname(cleaner.getSurname())
                        .phoneNumber(client.getPhoneNumber())
                        .build())
                .cost(order.getCost())
                .duration(order.getDuration())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private void employeeAppointment(Order order) {
        checkingForDuration(order);
        List<Schedule> listSchedule = scheduledRepo.
                getAllForAreaAndTimeOrders(order.getId(), order.getDuration(), dayOfWeek(order));

        if (!listSchedule.isEmpty()) {
            order.setCleaner(listSchedule.get(0).getCleaner());
            order.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        } else {
            order.setOrderStatus(OrderStatus.NO_EMPLOYEE);
        }

        orderRepo.save(order);
    }

    private void calculateOrderDuration(Order order) {
        float minTime = (float) (Math.ceil(order.getArea() / 25f) * 0.5f);
        RoomType roomType = order.getRoomType();
        float duration;

        switch (order.getCleaningType()) {
            case REGULAR:
                duration = minTime * roomType.getRegular();
                break;
            case GENERAL:
                duration = minTime * roomType.getGeneral();
                break;
            case AFTER_REPAIR:
                duration = minTime * roomType.getAfterRepair();
                break;
            default:
                throw new RequestException(HttpStatus.BAD_REQUEST, "Cleaning type не соответствует ни одному из значения enum");
        }

        order.setDuration(duration);
        orderRepo.save(order);

    }

    private Order returnOrderIsItExistsFromUserDetails(ClientUserDetails userDetails, Long orderId) {
        Supplier<RequestException> requestExceptionSupplier = () -> new RequestException(HttpStatus.FORBIDDEN, "Заказ с таким Id не найден");

        Order order =  orderRepo
                .findById(orderId)
                .orElseThrow(requestExceptionSupplier);

        if (order.getCustomer() != userDetails.getClient()){
            throw requestExceptionSupplier.get();
        }
        return order;
    }

    private void costCalculation(Order order) {
        checkingForDuration(order);
        Integer cost = (int) (order.getDuration() / 0.5F * 1000);
        order.setCost(cost);
        orderRepo.save(order);
    }

    private void checkingForDuration(Order order) {
        if (order.getDuration() == null){
            calculateOrderDuration(order);
        }
    }

    private boolean isCreateOrder(OrderRequest orderRequest) {
        return (isCorrectArea(orderRequest.getArea()) &&
                isCorrectRoomType(orderRequest.getRoomType()) &&
                isCorrectTheDate(orderRequest.getTheDate()) &&
                isCorrectCleaningType(orderRequest.getCleaningType()) &&
                isCorrectStartTime(orderRequest.getStartTime()));

    }

    private boolean isEditOrder(OrderRequest orderRequest) {
        Float area = orderRequest.getArea();
        RoomType roomType = orderRequest.getRoomType();
        Date theDate = orderRequest.getTheDate();
        CleaningType cleaningType = orderRequest.getCleaningType();
        Short startTime = orderRequest.getStartTime();

        return ((area == null || isCorrectArea(area)) &&
                (roomType == null || isCorrectRoomType(roomType)) &&
                (theDate == null || isCorrectTheDate(theDate)) &&
                (cleaningType == null || isCorrectCleaningType(cleaningType)) &&
                (startTime == null || isCorrectStartTime(startTime)));
    }

    private Integer dayOfWeek(Order order) {
        LocalDate date = order.getTheDate().toLocalDate();
        return date.getDayOfWeek().get(ChronoField.DAY_OF_WEEK);
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
}
