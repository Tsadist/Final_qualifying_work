package com.example.kyrsovay.service;

import com.example.kyrsovay.models.DB.Cleaner;
import com.example.kyrsovay.models.DB.Client;
import com.example.kyrsovay.models.DB.Order;
import com.example.kyrsovay.models.DB.Schedule;
import com.example.kyrsovay.models.enums.CleaningType;
import com.example.kyrsovay.models.enums.OrderStatus;
import com.example.kyrsovay.models.enums.RoomType;
import com.example.kyrsovay.models.response.CleanerResponse;
import com.example.kyrsovay.models.response.OrderResponse;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.repository.ScheduledRepo;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
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

    public void pricing(Long id) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;

        Integer cost = hashMapCost(order.getDuration());
        order.setCost(cost);
        orderRepo.save(order);
    }

    public void employeeAppointment(long id) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;

        List<Schedule> listSchedule = scheduledRepo.
                getAllForAreaAndTimeOrders(id, order.getDuration(), dayOfWeek(id));

        if (!listSchedule.isEmpty()) {
            order.setCleaner(listSchedule.get(0).getCleaner());
            order.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        } else {
            order.setOrderStatus(OrderStatus.NO_EMPLOYEE);
        }

        orderRepo.save(order);
    }

    public void calculateOrderDuration(Long id) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;
        float minTime = getTimeFor(order.getArea());
        Float duration = null;

        switch (order.getRoomType().name()) {
            case "COMMERCIAL":
                duration = calculateDurationForCleaningType(order, minTime, 1f, 1.5f, 2f);
            case "RESIDENTIAL":
                duration = calculateDurationForCleaningType(order, minTime, 1.5f, 2f, 2.5f);
        }

        order.setDuration(duration);
        orderRepo.save(order);
    }

    public OrderResponse putField(Long orderId, Short startTime, Date theDate, Float area,
                                  CleaningType cleaningType, RoomType roomType) {

        Order order = orderRepo.findById(orderId).get();
        if (startTime != null) {
            order.setStartTime(startTime);
        }
        if (theDate != null) {
            order.setTheDate(theDate);
        }
        if (area != null) {
            order.setArea(area);
        }
        if (cleaningType != null) {
            order.setCleaningType(cleaningType);
        }
        if (roomType != null) {
            order.setRoomType(roomType);
        }
        orderRepo.save(order);

        return createOrderResponse(order);
    }

    private Integer dayOfWeek(Long id) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;
        LocalDate date = order.getTheDate().toLocalDate();
        return date.getDayOfWeek().get(ChronoField.DAY_OF_WEEK);
    }

    private Float calculateDurationForCleaningType(Order order, float minTime, float r, float g, float pr) {
        switch (order.getCleaningType()) {
            case REGULAR:
                return minTime * r;
            case GENERAL:
                return minTime * g;
            case AFTER_REPAIR:
                return minTime * pr;
        }
        return null;
    }

    private float getTimeFor(float area) {
        return (float) (Math.ceil(area / 25f) * 0.5f);
    }

    private Integer hashMapCost(Float duration) {
        return (int) (duration / 0.5F * 1000);

    }
}
