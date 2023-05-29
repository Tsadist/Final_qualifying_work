package com.example.FQW.service;

import com.example.FQW.config.CustomUserDetails;
import com.example.FQW.ex.RequestException;
import com.example.FQW.models.DB.AdditionService;
import com.example.FQW.models.DB.Order;
import com.example.FQW.models.DB.User;
import com.example.FQW.models.enums.CleaningType;
import com.example.FQW.models.enums.OrderStatus;
import com.example.FQW.models.enums.RoomType;
import com.example.FQW.models.request.OrderRequest;
import com.example.FQW.models.response.CleanerResponse;
import com.example.FQW.models.response.MessageResponse;
import com.example.FQW.models.response.OrderResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final ScheduledRepo scheduledRepo;
    private final VacationRepo vacationRepo;
    private final AdditionServiceRepo additionServiceRepo;

    public OrderResponse getOrder(CustomUserDetails userDetails, Long orderId) {
        Order order = returnOrderIsItExistsFromUserDetails(userDetails, orderId);
        return getOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders(CustomUserDetails userDetails) {
        List<Order> orderList = orderRepo.findAllByCustomerId(userDetails.getClient().getId());
        return orderList
                .stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse createOrder(CustomUserDetails userDetails, OrderRequest orderRequest) {
        if (isCreateOrder(orderRequest)) {
            Order order = new Order();
            order.setCustomer(userDetails.getClient());
            order.setArea(orderRequest.getArea());
            order.setRoomType(orderRequest.getRoomType());
            order.setCleaningType(orderRequest.getCleaningType());
            order.setTheDate(orderRequest.getTheDate());
            order.setStartTime(orderRequest.getStartTime());
            order.setAdditionServicesId(orderRequest.getAdditionServicesId());

            Order newOrder = orderRepo.save(order);
            calculateOrderDuration(newOrder);
            employeeAppointment(newOrder);
            costCalculation(newOrder);

            return getOrderResponse(newOrder);
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "Какой-то из параметров запроса нулевой или невалидный");
        }

    }

    public OrderResponse editOrder(CustomUserDetails userDetails, Long orderId, OrderRequest orderRequest) {
        Order order = returnOrderIsItExistsFromUserDetails(userDetails, orderId);

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

    public MessageResponse deleteOrder(CustomUserDetails userDetails, Long orderId) {
        Order order = returnOrderIsItExistsFromUserDetails(userDetails, orderId);
        orderRepo.delete(order);

        if (orderRepo.findById(orderId).isEmpty()) {
            return new MessageResponse("Заказ был успешно удален");
        } else {
            throw new RequestException(HttpStatus.NOT_IMPLEMENTED, "Не удалось удалить заказ");
        }
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

    private void employeeAppointment(Order order) {
        checkingForDuration(order);
        List<User> cleanersFromSchedule = scheduledRepo
                .findAllCleanerFromDayOfWeekAndDuration(order.getId());
        List<User> cleanersFromVacation = vacationRepo
                .findAllCleanerByDateOrder(order.getId());

        List<User> suitableCleaner  = cleanersFromSchedule.stream()
                .filter(cleaner -> !cleanersFromVacation.contains(cleaner))
                .collect(Collectors.toList());

        if (!suitableCleaner.isEmpty()) {
            order.setCleaner(suitableCleaner.get(0));
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

    private Order returnOrderIsItExistsFromUserDetails(CustomUserDetails userDetails, Long orderId) {
        Supplier<RequestException> requestExceptionSupplier = () -> new RequestException(HttpStatus.FORBIDDEN, "Заказ с таким Id не найден");

        Order order = orderRepo
                .findById(orderId)
                .orElseThrow(requestExceptionSupplier);

        if (order.getCustomer() != userDetails.getClient()) {
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
        if (order.getDuration() == null) {
            calculateOrderDuration(order);
        }
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
        List<Long> additionServicesId = orderRequest.getAdditionServicesId();

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

    private boolean isCorrectAdditionServices(List<Long> additionServicesId) {
        List<AdditionService> allAdditionServices = additionServiceRepo.findAll();
        List<Long> collect = allAdditionServices
                .stream()
                .map(AdditionService::getId)
                .collect(Collectors.toList());
        return collect.containsAll(additionServicesId);
    }
}
