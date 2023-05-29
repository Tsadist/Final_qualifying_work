package com.example.FQW.models.DB;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.HashMap;

@Getter
@Setter
@Entity
@TypeDefs({
//        @TypeDef(
//                name = "time-array",
//                typeClass = IntArrayType.class
//        ),
        @TypeDef(
                name = "jb",
                typeClass = JsonBinaryType.class
        )
})
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

    @Type(type = "jb")
    @Column(
            name = "obj_days",
            columnDefinition = "jsonb"
    )
    private HashMap<String, ScheduleHours> objDays;

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private User cleaner;

}
