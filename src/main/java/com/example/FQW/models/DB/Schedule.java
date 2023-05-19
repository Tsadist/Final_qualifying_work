package com.example.FQW.models.DB;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBlobType;
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
        @TypeDef(
                name = "time-array",
                typeClass = IntArrayType.class
        ),
        @TypeDef(
                name = "jb",
                typeClass = JsonBlobType.class
        )
})
@Table(name = "schedule")
@ToString
public class Schedule {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private Short dayOfWeek;
    @Type(type = "time-array")
    @Column(
            name = "hours_work",
            columnDefinition = "integer[]"
    )
    private int[] hours = new int[2];
    private int numberWeek;

    @Type(type = "jb")
    @Column(
            name = "obj_days",
            columnDefinition = "jsonb"
    )
    private HashMap<Long, int[]> objDays;

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private User cleaner;

}
