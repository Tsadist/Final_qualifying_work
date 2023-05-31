package com.example.FQW.models.DB;

import com.example.FQW.models.enums.StatusPayment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long idPayment;
    private String linkForPayment;

    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;

    private LocalDateTime time;
    private Integer sum;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
