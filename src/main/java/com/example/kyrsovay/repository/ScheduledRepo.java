package com.example.kyrsovay.repository;

import com.example.kyrsovay.models.DB.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduledRepo extends JpaRepository<Schedule, Long> {


    @Query(nativeQuery = true, value = "select sc.*\n" +
            "from schedule sc\n" +
            "         join \"order\" o on o.id = ?1\n" +
            "where (sc.hours_work[2] - sc.hours_work[1]) >= ?2\n" +
            "  AND sc.day_of_week = ?3\n" +
            "  AND o.start_time >= sc.hours_work[1]\n" +
            "  AND o.start_time + ?2 <= sc.hours_work[2]")
    List<Schedule> getAllForAreaAndTimeOrders(Long orderId,
                                              Float duration,
                                              Integer dayOfWeek);

}
