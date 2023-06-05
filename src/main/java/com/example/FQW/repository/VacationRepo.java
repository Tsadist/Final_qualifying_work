package com.example.FQW.repository;

import com.example.FQW.models.DB.User;
import com.example.FQW.models.DB.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacationRepo extends JpaRepository<Vacation, Long> {

    @Query(nativeQuery = true, value =
            """
                    SELECT DISTINCT vac.cleaner_id
                    FROM vacation vac
                    JOIN orders o on o.id = ?1
                    WHERE vac.start_day < o.the_date
                    AND vac.end_day > o.the_date;""")
    List<User> findAllCleanerByDateOrder(Long orderId);

    List<Vacation> getAllByCleanerId (Long cleanerId);
}
