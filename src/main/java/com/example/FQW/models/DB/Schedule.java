package com.example.FQW.models.DB;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.HashMap;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    public static class ScheduleHours {
        private int startTime;
        private int endTime;
    }

    private int numberWeek;

    @Type(JsonBinaryType.class)
    private HashMap<String, ScheduleHours> objDays;

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private User cleaner;

}
