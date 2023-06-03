package com.example.FQW.models.DB;

import com.example.FQW.models.enums.CleaningType;
import com.example.FQW.models.enums.OrderStatus;
import com.example.FQW.models.enums.RoomType;
import io.hypersistence.utils.hibernate.type.array.LongArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
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
    private String paymentUrl;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private User cleaner;

    @Type(LongArrayType.class)
    private Long[] additionServicesId;

}
