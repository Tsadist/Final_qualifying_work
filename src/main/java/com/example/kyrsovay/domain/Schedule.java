package com.example.kyrsovay.domain;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Getter
@Setter
@Entity
@TypeDefs({
        @TypeDef(
                name = "time-array",
                typeClass = IntArrayType.class
        )
})
@Table(name = "schedule")
@ToString
public class Schedule {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private int dayOfWeek;
    @Type(type = "time-array")
    @Column(
            name = "hours_work",
            columnDefinition = "integer[]"
    )
    private int[] hours = new int[2];

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private Cleaner cleaner;

}
