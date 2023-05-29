package com.example.FQW.repository;

import com.example.FQW.models.DB.User;
import com.example.FQW.models.DB.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacationRepo extends JpaRepository<Vacation, Long> {

    @Query(nativeQuery = true, value =
            "SELECT DISTINCT vac.cleaner_id\n" +
            "FROM vacation vac\n" +
            "JOIN orders o on o.id = ?1\n" +
            "WHERE vac.start_day < o.the_date\n" +
            "AND vac.end_day > o.the_date;")
    List<User> findAllCleanerByDateOrder(Long orderId);
}
