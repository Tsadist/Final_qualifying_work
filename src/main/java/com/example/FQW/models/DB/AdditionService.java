package com.example.FQW.models.DB;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "addition_service")
public class AdditionService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private int cost;
    private float duration;

//    @OneToMany(mappedBy = "addition_services")
//    private Order order;

}