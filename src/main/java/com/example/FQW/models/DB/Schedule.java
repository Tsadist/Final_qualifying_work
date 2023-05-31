package com.example.FQW.models.DB;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.util.HashMap;

@Getter
@Setter
@Entity
@Table(name = "schedule")
@ToString
public class Schedule {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;

    private int numberWeek;

    @Getter
    @Setter
    public static class ScheduleHours {
        private int startTime;
        private int endTime;
    }

    @Type(JsonBinaryType.class)
    @Column(
            name = "obj_days",
            columnDefinition = "jsonb"
    )
    private HashMap<String, ScheduleHours> objDays;

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private User cleaner;

}
