package com.example.kyrsovay.models;

import com.example.kyrsovay.models.enums.CleaningType;
import com.example.kyrsovay.models.enums.OrderStatus;
import com.example.kyrsovay.models.enums.RoomType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
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
    private Client customer;

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private Cleaner cleaner;

}
