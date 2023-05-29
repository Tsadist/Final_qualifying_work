package com.example.FQW.models.DB;

import com.example.FQW.models.enums.CleaningType;
import com.example.FQW.models.enums.OrderStatus;
import com.example.FQW.models.enums.RoomType;
import com.vladmihalcea.hibernate.type.array.LongArrayType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@TypeDefs({
        @TypeDef(
                name = "larr",
                typeClass = LongArrayType.class
        )
})
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private CleaningType cleaningType;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private float area;
    private Date theDate;
    private Short startTime;
    private Float duration;
    private Integer cost;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private User cleaner;

    @Type(type = "larr")
    private List<Long> additionServicesId;

//    @ManyToOne
//    @JoinColumn(name = "addition_services_id")
//    private List<AdditionService> additionServices = new ArrayList<>();


}
