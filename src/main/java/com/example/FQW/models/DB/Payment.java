package com.example.FQW.models.DB;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String idPayment;
    private String linkForPayment;
    private String statusPayment;
    private String time;
    private String sum;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Payment() {

    }
}
