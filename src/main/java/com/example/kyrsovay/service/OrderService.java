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
import com.example.kyrsovay.models.response.OrderResponse;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.repository.ScheduledRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final ScheduledRepo scheduledRepo;
    private static Client client = new Client();

    public OrderService(OrderRepo orderRepo,
                        ScheduledRepo scheduledRepo) {
        this.orderRepo = orderRepo;
        this.scheduledRepo = scheduledRepo;
    }

    public OrderResponse createOrderResponse(Order order) {
        Cleaner cleaner = order.getCleaner();

        if (cleaner != null){
            client = cleaner.getClient();
        } else {
            cleaner = new Cleaner();
        }

//        client = cleaner.getClient();

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

    public Long createOrder(ClientUserDetails userDetails, OrderRequest orderRequest) {

        if (isCreateOrder(orderRequest)){
            Order order = new Order();
            order.setCustomer(userDetails.getClient());
            order.setArea(orderRequest.getArea());
            order.setRoomType(orderRequest.getRoomType());
            order.setCleaningType(orderRequest.getCleaningType());
            order.setTheDate(orderRequest.getTheDate());
            order.setStartTime(orderRequest.getStartTime());
            order = orderRepo.save(order);

            Long newOrderId = order.getId();
            calculateOrderDuration(newOrderId);
            employeeAppointment(newOrderId);
            pricing(newOrderId);

            return newOrderId;
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Какой-то из параметров запроса нулевой или невалидный");
        }

    }

    public boolean editOrder(Long orderId, OrderRequest orderRequest){

        Order order = orderRepo.findById(orderId).get();

        if (isEditOrder(orderRequest)){
            order.setCleaningType(orderRequest.getCleaningType());
            order.setArea(orderRequest.getArea());
            order.setRoomType(orderRequest.getRoomType());
            order.setTheDate(orderRequest.getTheDate());
            order.setStartTime(orderRequest.getStartTime());
            orderRepo.save(order);

            return true;
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Все параметры запроса нулевые или невалидные");
        }
    }

    public void checkingOrderId(Long orderId) {
        if (orderId <= 0) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Id меньше 0");
        } else if (orderRepo.findById(orderId).isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Заказ с таким Id не найден");
        }
    }


    private void pricing(Long orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);

        Integer cost = hashMapCost(order.getDuration());
        order.setCost(cost);
        orderRepo.save(order);
    }

    private void employeeAppointment(Long orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);

        List<Schedule> listSchedule = scheduledRepo.
                getAllForAreaAndTimeOrders(orderId, order.getDuration(), dayOfWeek(orderId));

        if (!listSchedule.isEmpty()) {
            order.setCleaner(listSchedule.get(0).getCleaner());
            order.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        } else {
            order.setOrderStatus(OrderStatus.NO_EMPLOYEE);
        }

        orderRepo.save(order);
    }

    private void calculateOrderDuration(Long orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);

        float minTime = getTimeFor(order.getArea());
        RoomType roomType = order.getRoomType();
        Float duration = null;

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

    private Integer dayOfWeek(Long id) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;
        LocalDate date = order.getTheDate().toLocalDate();
        return date.getDayOfWeek().get(ChronoField.DAY_OF_WEEK);
    }

    private float getTimeFor(float area) {
        return (float) (Math.ceil(area / 25f) * 0.5f);
    }

    private Integer hashMapCost(Float duration) {
        return (int) (duration / 0.5F * 1000);

    }

    private static boolean isCreateOrder(OrderRequest orderRequest) {

        return (isCorrectArea(orderRequest.getArea()) &&
                isCorrectRoomType(orderRequest.getRoomType()) &&
                isCorrectTheDate(orderRequest.getTheDate()) &&
                isCorrectCleaningType(orderRequest.getCleaningType()) &&
                isCorrectStartTime(orderRequest.getStartTime()));

    }

    private static boolean isEditOrder(OrderRequest orderRequest) {

        return (isCorrectArea(orderRequest.getArea()) ||
                isCorrectRoomType(orderRequest.getRoomType()) ||
                isCorrectTheDate(orderRequest.getTheDate()) ||
                isCorrectCleaningType(orderRequest.getCleaningType()) ||
                isCorrectStartTime(orderRequest.getStartTime()));
    }

    private static boolean isCorrectTheDate(Date theDate) {
        return (theDate != null &&
                !theDate.before(new java.util.Date()));
    }

    private static boolean isCorrectStartTime(Short startTime) {
        return (startTime != null &&
                startTime >= 8 && startTime <= 22);
    }

    private static boolean isCorrectArea(Float area) {
        return (area != null &&
                area > 10);
    }

    private static boolean isCorrectCleaningType(CleaningType cleaningType) {
        return (cleaningType != null &&
                Arrays.toString(CleaningType.values()).contains(cleaningType.toString()));
    }

    private static boolean isCorrectRoomType(RoomType roomType) {
        return (roomType != null &&
                Arrays.toString(RoomType.values()).contains(roomType.toString()));
    }
}
