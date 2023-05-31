package com.example.FQW.models.DB;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.sql.Date;

@Setter
@Getter
@Entity
@Table(name = "vacation")
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date startDay;
    private Date endDay;
    private Long managerId;
    private Long cleanerId;
}
