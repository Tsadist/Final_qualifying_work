package com.example.kyrsovay.service;

import com.example.kyrsovay.domain.Order;
import com.example.kyrsovay.domain.Schedule;
import com.example.kyrsovay.domain.enums.OrderStatus;
import com.example.kyrsovay.repository.OrderRepo;
import com.example.kyrsovay.repository.ScheduledRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final ScheduledRepo scheduledRepo;

    public OrderService(OrderRepo orderRepo,
                        ScheduledRepo scheduledRepo
    ) {
        this.orderRepo = orderRepo;
        this.scheduledRepo = scheduledRepo;
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
            order.setEmployee(listSchedule.get(0).getEmployee());
            order.setOrderStatus(OrderStatus.Ждет_оплаты);
        } else {
            order.setOrderStatus(OrderStatus.Нет_сотрудника);
        }

        orderRepo.save(order);
    }

    public void calculateOrderDuration(Long id) {
        Order order = orderRepo.findById(id).orElse(null);
        assert order != null;
        float minTime = getTimeFor(order.getArea());
        Float duration = null;

        switch (order.getRoomType().name()) {
            case "Коммерческое":
                duration = calculateDurationForCleaningType(order, minTime, 1f, 1.5f, 2f);
            case "Жилое":
                duration = calculateDurationForCleaningType(order, minTime, 1.5f, 2f, 2.5f);
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

    private Float calculateDurationForCleaningType(Order order, float minTime, float r, float g, float pr) {
        switch (order.getCleaningType().name()) {
            case "Регулярная":
                return minTime * r;
            case "Генеральная":
                return minTime * g;
            case "После_ремонта":
                return minTime * pr;
        }
        return null;
    }

    private float getTimeFor(float area) {
        return (float)(Math.ceil(area / 25f) * 0.5f);
    }

    private Integer hashMapCost(Float duration) {
        return (int)(duration / 0.5F * 1000);

    }

}
