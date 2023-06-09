package com.example.FQW.repository;

import com.example.FQW.models.DB.Schedule;
import com.example.FQW.models.DB.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduledRepo extends JpaRepository<Schedule, Long> {

    @Query(nativeQuery = true, value =
            """
                    SELECT DISTINCT sc.cleaner_id FROM schedule sc\s
                    JOIN orders o on o.id =?1\s
                    WHERE o.start_time>= to_number(sc.obj_days #>>
                    cast('{' || extract(dow from o.the_date) || ',startTime}'AS text[]),'99')
                    AND o.start_time +o.duration <= to_number(sc.obj_days #>>
                    cast('{' || extract(dow from o.the_date) || ',endTime}'AS text[]), '99')""")
    List<Long> findAllCleanerFromDayOfWeekAndDuration(Long orderId);

    List<Schedule> findAllByCleanerId(Long cleanerId);

    Schedule findByCleanerIdAndNumberWeek(Long cleanerId, int numberWeek);

    void deleteAllByCleanerId(Long cleanerId);
}

