package com.example.FQW.models.DB;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "addition_service")
public class AdditionService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private Integer cost;
    private Float duration;

}